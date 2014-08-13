package net.alfss.smsserver.sms;

import com.google.common.util.concurrent.RateLimiter;
import net.alfss.smsserver.config.GlobalConfig;
import net.alfss.smsserver.database.dao.impl.MessageDAOImpl;
import net.alfss.smsserver.database.entity.Channel;
import net.alfss.smsserver.database.entity.Message;
import net.alfss.smsserver.database.exceptions.DatabaseError;
import net.alfss.smsserver.rabbit.exceptions.RabbitMqException;
import net.alfss.smsserver.rabbit.exceptions.RabbitMqQueueConnectException;
import net.alfss.smsserver.rabbit.queue.QueueDirectSend;
import net.alfss.smsserver.sms.exceptions.SmsDestinationAddressWrongLength;
import net.alfss.smsserver.sms.exceptions.SmsServerConnectionException;
import net.alfss.smsserver.sms.exceptions.SmsServerException;
import net.alfss.smsserver.sms.exceptions.SmsSourceAddressWrongLength;
import net.alfss.smsserver.sms.pool.SmsServerConnectPool;
import net.alfss.smsserver.sms.prototype.AsyncSmsServerChild;
import org.smpp.Data;
import org.smpp.TimeoutException;
import org.smpp.WrongSessionStateException;
import org.smpp.pdu.Address;
import org.smpp.pdu.PDUException;
import org.smpp.pdu.SubmitSM;
import org.smpp.pdu.WrongLengthOfStringException;
import org.smpp.util.ByteBuffer;
import org.smpp.util.NotEnoughDataInByteBufferException;
import org.smpp.util.TerminatingZeroNotFoundException;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: alfss
 * Date: 28.11.13
 * Time: 13:13
 */
public class AsyncSmsServerSubmitter extends AsyncSmsServerChild {
    private final QueueDirectSend sendQueue;
    private final MessageDAOImpl messageDAO;

    public AsyncSmsServerSubmitter(GlobalConfig config, Channel channel, SmsServerConnectPool connectPool, RateLimiter rateLimiter, AtomicInteger seqNumber) {
        super(config, channel, connectPool, rateLimiter, seqNumber);
        this.messageDAO = new MessageDAOImpl();
        this.sendQueue = new QueueDirectSend(config, channel, true);
    }

    @Override
    public void run() {
        setRunning(true);
        errorMessage("start (channel = " + channel.getName() + ")");
        do {
            try {
                waitMessage();
                rateLimiter.acquire();
            } catch (SmsServerException e) {
                errorMessage("error Invalidated object", e);
                waitRecconectSmpp();
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


    private void waitMessage() {
        int id = Integer.parseInt(sendQueue.getNextMessage());
        Message message = messageDAO.get(id);
        message.setSequenceNumber(seqNumber.incrementAndGet());

        SubmitSM request = null;

        try {
            request = createSubmitRequest(message);
        } catch (Exception e) {
            debugMessage("error create submit", e);
        }

        try {
            if(request != null) {
                session = connectPool.getResource();
                submit(request);
                connectPool.returnResource(session);
            }
            messageDAO.setStatusWaitResponse(message);
        } catch (SmsServerException e) {
            errorMessage("error connect to smpp", e);
            errorMessage("retrun message id = " +  message.getMessageId() + " to queue");
            sendQueue.publish(String.valueOf(message.getMessageId()));
            connectPool.returnBrokenResource(session);
        }

    }


    private Address createAddress(String address) throws WrongLengthOfStringException {
        Address addressInst = new Address();
        if (address == null) {
            addressInst.setTon((byte) channel.getSmppSourceTon()); // national ton
            addressInst.setNpi((byte) channel.getSmppSourceNpi()); // numeric plan indicator
            addressInst.setAddress(channel.getSmppSourceAddr(), Data.SM_ADDR_LEN);
        } else {
            addressInst.setTon((byte) channel.getSmppDestTon()); // national ton
            addressInst.setNpi((byte) channel.getSmppDestNpi()); // numeric plan indicator
            addressInst.setAddress(address, Data.SM_ADDR_LEN);
        }
        return addressInst;
    }


    public SubmitSM createSubmitRequest(Message message) throws SmsDestinationAddressWrongLength, SmsSourceAddressWrongLength, TerminatingZeroNotFoundException, PDUException, NotEnoughDataInByteBufferException {

        SubmitSM request = new SubmitSM();
        request.setDataCoding((byte) 0x08);
        try {
            if (message.getFrom() != null) {
                request.setSourceAddr(message.getFrom());
            } else {
                request.setSourceAddr(createAddress(null));
            }
        } catch (WrongLengthOfStringException e) {
            throw new SmsSourceAddressWrongLength(e);
        }

        try {
            request.setDestAddr(createAddress(message.getTo()));
        } catch (WrongLengthOfStringException e) {
            throw new SmsDestinationAddressWrongLength(e);
        }
        request.setSequenceNumber(message.getSequenceNumber());
        request.setEsmClass(message.getEsmClass());

        request.setRegisteredDelivery(Data.SM_SMSC_RECEIPT_REQUESTED);

        if (message.isPayload()) {
            request.setMessagePayload(new ByteBuffer(message.getMessageData()));
        } else {
            request.setShortMessageData(new ByteBuffer(message.getMessageData()));
        }

        return request;
    }

    private void submit(SubmitSM request) {
        try {
            session.submit(request);
        } catch (PDUException | TimeoutException | WrongSessionStateException | IOException | NullPointerException e) {
            throw new SmsServerConnectionException("error submit", e);
        }
    }
}
