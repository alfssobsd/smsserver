package net.alfss.smsserver.sms;

import com.google.common.util.concurrent.RateLimiter;
import net.alfss.smsserver.config.GlobalConfig;
import net.alfss.smsserver.database.dao.impl.MessageDAOImpl;
import net.alfss.smsserver.database.dao.impl.MessageStatusDAOImpl;
import net.alfss.smsserver.database.entity.Channel;
import net.alfss.smsserver.database.entity.Message;
import net.alfss.smsserver.database.entity.MessageStatus;
import net.alfss.smsserver.database.exceptions.DatabaseError;
import net.alfss.smsserver.rabbit.data.InboundMessage;
import net.alfss.smsserver.rabbit.exceptions.RabbitMqException;
import net.alfss.smsserver.rabbit.exceptions.RabbitMqQueueConnectException;
import net.alfss.smsserver.rabbit.exceptions.RabbitMqQueueMappingException;
import net.alfss.smsserver.rabbit.queue.QueueDirectInbound;
import net.alfss.smsserver.rabbit.queue.QueueDirectSend;
import net.alfss.smsserver.sms.pool.SmsServerConnectPool;
import net.alfss.smsserver.sms.prototype.AsyncSmsServerChild;
import org.smpp.Data;
import org.smpp.util.ByteBuffer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: alfss
 * Date: 26.11.13
 * Time: 16:24
 */
public class AsyncSmsServerPrepareMessage  extends AsyncSmsServerChild {

    private final QueueDirectInbound inboundQueue;
    private final MessageDAOImpl messageDAO;
    private final MessageStatusDAOImpl statusDAO;
    private final QueueDirectSend sendQueue;
    private InboundMessage inboundMessage = null;
    private byte uniqIdMessage = (byte) 0x00;


    public AsyncSmsServerPrepareMessage(GlobalConfig config,
                                        Channel channel,
                                        SmsServerConnectPool connectPool,
                                        RateLimiter rateLimiter,
                                        AtomicInteger seqNumber) {
        super(config, channel, connectPool, rateLimiter, seqNumber);
        this.inboundQueue = new QueueDirectInbound(config, channel, true);
        this.sendQueue = new QueueDirectSend(config, channel, false);

        this.messageDAO = new MessageDAOImpl();
        this.statusDAO = new MessageStatusDAOImpl();
    }


    @Override
    public void run() {
        setRunning(true);
        errorMessage("start (channel = " + channel.getName() + ")");
        do {
            try {
                processingMessage();
            } catch (RabbitMqQueueConnectException | RabbitMqException e) {
                waitRecconectRabbitMq(sendQueue);
            } catch (DatabaseError e) {
                errorMessage("error ", e);
                waitRecconectDatabase();
            } catch (NullPointerException e) {
                debugMessage("NullPointerException", e);
            } catch (Exception e) {
                debugMessage("WTF Exception!!! " + channel.getName() + " ", e);
            }
        } while (isRunning() & !isInterrupted());
    }

    //TODO:нужно возвращать в inboundQueue сообшения если есть проблемы!
    private void processingMessage() {

        //get inboundMessage
        try {
            if (inboundMessage == null) {
                inboundMessage = inboundQueue.getNextMessage();
                debugMessage("get InboundMessage (channel = " + channel.getName() + ")");
            }
        } catch (RabbitMqQueueMappingException e ){
            errorMessage("error parse InboundMessage (channel = " + channel.getName() + ") ", e);
            inboundMessage = null;
        } catch (RabbitMqQueueConnectException e) {
            errorMessage("error read InboundMessage (channel = " + channel.getName() + ") ", e);
        } catch (Exception e) {
            debugMessage("WTF Exception!!! " + channel.getName() + " ", e);
        }

        //push message
        if (inboundMessage != null) {
            debugMessage("create message (channel = " + channel.getName() + ")");
            try {
                ByteBuffer[] preparedMessage = getPrepareMessage(inboundMessage);
                if (channel.isPayload()) {
                    createMessage(preparedMessage[0], (byte) 0x00);
                } else {
                    if (preparedMessage.length > 1) {
                        for (int i = inboundMessage.getStartPart(); i < preparedMessage.length; i++) {
                            inboundMessage.setStartPart(i);
                            createMessage(preparedMessage[i], (byte) 0x40);
                        }
                    } else {
                        createMessage(preparedMessage[0], (byte) 0x00);
                    }
                }
                inboundMessage = null;
            } catch (UnsupportedEncodingException e) {
                errorMessage("UnsupportedEncodingException (channel = " + channel.getName() + ") ", e);
                inboundMessage = null;
            } catch (RabbitMqQueueConnectException e) {
                errorMessage("error write id message to rabbitmq (channel = " + channel.getName() + ") ", e);
            } catch (Exception e) {
                debugMessage("WTF Exception!!! " + channel.getName() + " ", e);
                inboundMessage = null;
            }
        }
    }

    private void createMessage(ByteBuffer byteMessage, byte esmClass) throws IOException {
        MessageStatus messageStatus = statusDAO.getByName("WAITING_SEND");
        Message message = new Message();
        message.setPayload(channel.isPayload());
        message.setQueueName(channel.getQueueName());
        message.setChannel(channel);
        message.setEsmClass(esmClass);
        message.setFrom(inboundMessage.getFrom());
        message.setTo(inboundMessage.getTo());
        message.setMessageData(byteMessage.getBuffer());
        message.setMessageStatus(messageStatus);
        sendQueue.publish(String.valueOf(messageDAO.create(message)));
    }

    private byte getNextUniqIdMessage() {
        if (uniqIdMessage == (byte) 0x00) {
            uniqIdMessage++;
        }
        return uniqIdMessage++;
    }

    private ByteBuffer[] getPrepareMessage(InboundMessage message) throws UnsupportedEncodingException {
        ArrayList<String> list = new ArrayList<>();
        String str = message.getMessageText();
        if (message.getUniqueMessageNumber() == null) message.setUniqueMessageNumber((int) getNextUniqIdMessage());
        int maxMessages = channel.getSmppMaxSplitMessage();
        int countMessage = 0;

        if (channel.isPayload() || str.length() < 70) {
            ByteBuffer[] arr = new ByteBuffer[1];
            ByteBuffer byteMessage = new ByteBuffer();
            byteMessage.appendString(str, Data.ENC_UTF16_BE);
            arr[0] = byteMessage;

            return arr;
        } else {
            while (str.length() > 67) {
                String s = str.substring(0, 67);
                str = str.substring(67);
                try {
                    if (countMessage < maxMessages) {
                        list.add(s);
                        countMessage++;
                    }
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
            }

            if (countMessage < maxMessages) {
                try {
                    list.add(str);
                } catch (Exception uce) {
                    uce.printStackTrace();
                }
            }

            ByteBuffer[] arr = new ByteBuffer[list.size()];

            if (list.size() == 1) {
                ByteBuffer byteMessage = new ByteBuffer();
                byteMessage.appendString(list.get(0), Data.ENC_UTF16_BE);
                arr[0] = byteMessage;

                return arr;
            }

            for (int i = 0; i < arr.length; i++) {
                ByteBuffer byteMessage = new ByteBuffer();
                byteMessage.appendByte((byte) 0x05);
                byteMessage.appendByte((byte) 0x00);
                byteMessage.appendByte((byte) 0x03);
                byteMessage.appendByte(Byte.valueOf(message.getUniqueMessageNumber().toString()));
                byteMessage.appendByte((byte) arr.length);
                byteMessage.appendByte((byte) (i + 1));
                byteMessage.appendString(list.get(i), Data.ENC_UTF16_BE);
                arr[i] = byteMessage;
            }

            return arr;
        }
    }
}
