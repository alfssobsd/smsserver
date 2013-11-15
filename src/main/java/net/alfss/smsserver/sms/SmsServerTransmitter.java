package net.alfss.smsserver.sms;

import com.google.common.util.concurrent.RateLimiter;
import net.alfss.smsserver.config.ChannelConfig;
import net.alfss.smsserver.message.Message;
import net.alfss.smsserver.redis.RedisClient;
import net.alfss.smsserver.redis.exceptions.RedisUnknownError;
import net.alfss.smsserver.sms.exceptions.SmsServerConnectingError;
import net.alfss.smsserver.sms.exceptions.SmsServerMessageError;
import net.alfss.smsserver.sms.exceptions.SmsServerNeedWait;
import net.alfss.smsserver.sms.logger.FailSend;
import net.alfss.smsserver.sms.logger.SuccessSend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smpp.*;
import org.smpp.pdu.*;
import org.smpp.pdu.tlv.TLVException;
import org.smpp.util.ByteBuffer;
import org.smpp.util.NotEnoughDataInByteBufferException;
import org.smpp.util.TerminatingZeroNotFoundException;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * User: alfss
 * Date: 03.10.13
 * Time: 16:26
 */
public class SmsServerTransmitter extends Thread {

    private BindRequest bindRequest;
    private Session session;
    private int seqNumber = 2;
    private byte uniqIdMessage = (byte) 0x00;
    private boolean isConnect = false;
    final Logger logger = (Logger) LoggerFactory.getLogger(SmsServerTransmitter.class);
    private final ChannelConfig config;
    private final RedisClient redisClient;
    private RateLimiter rateLimiter;
    private SuccessSend successSendLogger;
    private FailSend failSendLogger;

    public SmsServerTransmitter(ChannelConfig config, RedisClient redisClient) {
        this.config = config;
        this.redisClient = redisClient;
        rateLimiter = RateLimiter.create(config.getMessagePerScond());
        bindRequest = new BindTransmitter();

        this.successSendLogger = new SuccessSend();
        this.failSendLogger = new FailSend();
    }

    @Override
    public void run() {
        logger.error("SmsServerTransmitter: Start (channel = " + config.getChannel() + ")");
        waitMessage();
    }

    private void waitMessage() {
        while (!isConnect) {
            try {
                this.connect();
            } catch (SmsServerConnectingError e) {
                logger.error("SmsServerTransmitter: init wait message error (channel = " +
                        config.getChannel() + ") " + e.toString());
                try {
                    sleep(1000 * config.getReconnectTimeOut());
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }

        while (true) {
            logger.debug("SmsServerTransmitter: wait message (channel = " + config.getChannel() + ")");
            Message message = null;
            try {
                message = redisClient.popBlockMessageFromList(config.getEnquireLinkInterval(), config.getChannelQueue());
                logger.debug("SmsServerTransmitter: get message (channel = " + config.getChannel() + ")");
                if (message != null) {
                    rateLimiter.acquire();
                    if (message.isExpired()) {
                        throw new SmsServerMessageError("message expire");
                    }
                    sendMessage(message);
                } else {
                    enquireLinkRequest();
                }
            } catch (SmsServerConnectingError e) {
                logger.error("SmsServerTransmitter: connecting error (channel = " +
                        config.getChannel() + ") " + e.toString());
                returnMessagToList(message);
                reconnect();
            } catch (SmsServerNeedWait e) {
                logger.error("SmsServerTransmitter: need wait (channel = " +
                        config.getChannel() + ") " + e.toString());
                returnMessagToList(message);
                try {
                    sleep(config.getWaitResendTimeOut() * 1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            } catch (SmsServerMessageError e) {
                logger.error("SmsServerTransmitter: error message (channel = " +
                        config.getChannel() + ") " + e.toString());
                failSendLogger.writeLog(message);
            } catch (JedisConnectionException e) {
                logger.error("SmsServerTransmitter: error connect to redis (channel = " +
                        config.getChannel() + ") " + e.toString());
                try {
                    sleep(redisClient.getConnectTimeOutInMs());
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            } catch (JedisDataException e) {
                logger.error("SmsServerTransmitter: error read data from redis (channel = " +
                        config.getChannel() + ") " + e.toString());
            } catch (RedisUnknownError e) {
                logger.error("SmsServerTransmitter: unknown error redis (channel = " +
                        config.getChannel() + ") " + e.toString());
            }
        }
    }

    private void connect() throws SmsServerConnectingError {
        if (!isConnect) {

            Connection conn = new TCPIPConnection(config.getHost(), config.getSendPort());
            session = new Session(conn);

            try {
                bindRequest.setSystemId(config.getUser());
            } catch (WrongLengthOfStringException e) {
                logger.error("SmsServerTransmitter: connect user wrong length (channel = " +
                        config.getChannel() + ") " + e.toString());
                throw new SmsServerConnectingError("Error connect");
            }
            try {
                bindRequest.setPassword(config.getPassword());
            } catch (WrongLengthOfStringException e) {
                logger.error("SmsServerTransmitter: connect password wrong length (channel = " +
                        config.getChannel() + ") " + e.toString());
                throw new SmsServerConnectingError("Error connect");
            }
            try {
                bindRequest.setSystemType(config.getSystemType());
            } catch (WrongLengthOfStringException e) {
                logger.error("SmsServerTransmitter: connect system-type wrong length (channel = " +
                        config.getChannel() + ") " + e.toString());
                throw new SmsServerConnectingError("Error connect");
            }

            bindRequest.setAddressRange(config.getAddressRange());
            try {
                Response response = session.bind(bindRequest);
                if(response.getCommandStatus() == Data.ESME_ROK) {
                    logger.error("SmsServerTransmitter: connected to " +
                            config.getHost() + " (channel =  " + config.getChannel() + ")");
                    isConnect = true;
                }
            } catch (TimeoutException | PDUException | IOException | WrongSessionStateException e) {
                logger.error("SmsServerTransmitter: connect (channel = " +
                        config.getChannel() + ") " + e.toString());
            }

            if(!isConnect) {
                throw new SmsServerConnectingError("Error connect");
            }
        }
    }

    private void disconnect() {
        try {
            if(isConnect) {
                isConnect = false;
                session.unbind();
            }
        } catch (TimeoutException | PDUException | IOException | WrongSessionStateException e) {
            logger.error("SmsServerTransmitter: disconnect (channel = " +
                    config.getChannel() + ") " + e.toString());
        }
    }

    private void reconnect() {
        if(isConnect) {
            disconnect();
        }
        try {
            logger.error("SmsServerTransmitter: reconnect after " +
                    config.getReconnectTimeOut() + " seconds (channel =  " + config.getChannel() + ")");
            sleep(1000 * config.getReconnectTimeOut());
            connect();
        } catch (InterruptedException | SmsServerConnectingError e) {
            logger.error("SmsServerTransmitter: recconect error (channel = " +
                    config.getChannel() + ") " + e.toString());
        }
    }

    private void returnMessagToList(Message message) {
        if (message != null)  {
            try {
                redisClient.pushMessageToHeadList(message);
            } catch (NullPointerException e1) {
                logger.error("SmsServerTransmitter: connecting error -> error push to redis (channel = " +
                        config.getChannel() + ") message =  " +  message.toString() + " " + e1.toString());
                failSendLogger.writeLog(message);
            } catch (RedisUnknownError e1) {
                logger.error("SmsServerTransmitter: unknown error redis (channel = " +
                        config.getChannel() + ") message =  " + message.toString() + " " + e1.toString());
                failSendLogger.writeLog(message);
            }
        }
    }

    private void enquireLinkRequest() throws SmsServerConnectingError {
        EnquireLink request = new EnquireLink();
        request.setSequenceNumber(this.getNextSeqNumber());
        try {
            EnquireLinkResp response = session.enquireLink(request);
            if(response.getCommandStatus() != Data.ESME_ROK) {
                logger.error("SmsServerTransmitter: enquireLinkRequest error status (channel =  " +
                        config.getChannel() + ") status = " + response.getCommandStatus());
                throw new SmsServerConnectingError("Error enquireLink");
            }
            logger.debug("SmsServerTransmitter: enquireLinkRequest success (channel =  " +
                    config.getChannel() + ") status = " + response.getCommandStatus());
        } catch (TimeoutException | PDUException | IOException | WrongSessionStateException | NullPointerException e) {
            logger.error("SmsServerTransmitter: enquireLinkRequest (channel =  " +
                    config.getChannel() + ") " + e.toString());
            throw new SmsServerConnectingError("Error enquireLink");
        }

    }

    private SubmitSM createSubmitRequest(Message message, byte esmClass) throws SmsServerConnectingError, SmsServerMessageError {

        SubmitSM request = new SubmitSM();
        request.setDataCoding((byte) 0x08);
        try {
            if (message.getSourceAddress() != null) {
                request.setSourceAddr(message.getSourceAddress());
            } else {
                request.setSourceAddr(this.createAddress(null));
            }
        } catch (WrongLengthOfStringException e) {
            throw new SmsServerConnectingError("source address " + config.getSoureceAddress() + "," + e.toString());
        }

        try {
            request.setDestAddr(this.createAddress(message.getDestinationAddress()));
        } catch (WrongLengthOfStringException e) {
            throw new SmsServerMessageError("destination address " + message.getDestinationAddress() + "," + e.toString());
        }
        request.setSequenceNumber(this.getNextSeqNumber());
        request.setEsmClass(esmClass);

        return request;
    }

    private void runSubmitRequest(SubmitSM request) throws PDUException, TimeoutException,
            WrongSessionStateException, IOException, SmsServerConnectingError, SmsServerNeedWait {
        try {
            SubmitSMResp response = session.submit(request);
            //TODO:get message id
            //response.getMessageId();
            switch (response.getCommandStatus()) {
                case Data.ESME_ROK:
                    logger.debug("SmsServerTransmitter: sendMessage messageId = submission submitted  (channel =  " +
                            config.getChannel() + ") status = " + response.getCommandStatus());
                    break;
                case Data.ESME_RMSGQFUL:
                    logger.error("SmsServerTransmitter: smpp server queue is full  (channel =  " +
                            config.getChannel() + ") status = " + response.getCommandStatus());
                    throw new SmsServerNeedWait("submit ESME_RMSGQFUL");
                default:
                    logger.error("SmsServerTransmitter: sendMessage submission failed  (channel =  " +
                            config.getChannel() + ") status = " + response.getCommandStatus());
                    throw new SmsServerConnectingError("submit unknown error");
            }
        } catch (NullPointerException e) {
            throw new SmsServerConnectingError("WTF null pointer submit requrest " + e.toString());
        }




    }


    private boolean sendMessage(Message message) throws SmsServerConnectingError, SmsServerMessageError, SmsServerNeedWait {

        byte esmClass = (byte) 0x00;

        ByteBuffer[] preparedMessage;
        try {
            preparedMessage = prepareMessage(message, config.getEnablePlayLoad());
        } catch (UnsupportedEncodingException | NullPointerException e) {
            throw new SmsServerMessageError("prepareMessage " + e.toString());
        }

        if (preparedMessage.length > 1) {
            esmClass = (byte) 0x40;
        }

        try {
            if (config.getEnablePlayLoad()) {
                SubmitSM request = createSubmitRequest(message, esmClass);
                request.setMessagePayload(preparedMessage[0]);
                runSubmitRequest(request);
            } else {
                if (preparedMessage.length > 1) {
                    for (int i = message.getStartPart(); i < preparedMessage.length; i++) {
                        message.setStartPart(i);
                        SubmitSM request = createSubmitRequest(message, esmClass);
                        //TODO: разобраться с TLV
                        if ((i + 1) < preparedMessage.length) {
                            try {
                                request.setExtraOptional((short) 1062, new ByteBuffer(new byte[]{(byte) 0x01}));
                            } catch (TLVException e) {
                                e.printStackTrace();
                            }
                        }
                        request.setShortMessageData(preparedMessage[i]);
                        runSubmitRequest(request);
                    }
                } else {
                    SubmitSM request = createSubmitRequest(message, esmClass);
                    request.setShortMessageData(preparedMessage[0]);
                    runSubmitRequest(request);
                }

            }
        } catch (TimeoutException | PDUException | IOException | WrongSessionStateException| NullPointerException e) {
            throw new SmsServerConnectingError(e.toString());
        } catch (NotEnoughDataInByteBufferException | TerminatingZeroNotFoundException e) {
            throw new SmsServerMessageError(e.toString());
        }

        successSendLogger.writeLog(message);

        return  true;
    }


    private int getNextSeqNumber() {
        return seqNumber++;
    }

    private byte getNextUniqIdMessage() {
        if (uniqIdMessage == (byte) 0x00) {
            uniqIdMessage++;
        }
        return uniqIdMessage++;
    }

    private Address createAddress(String address)
            throws WrongLengthOfStringException {
        Address addressInst = new Address();
        if (address == null) {
            addressInst.setTon((byte) config.getSourceAddrTon()); // national ton
            addressInst.setNpi((byte) config.getSourceAddrNpi()); // numeric plan indicator
            addressInst.setAddress(config.getSoureceAddress(), Data.SM_ADDR_LEN);
        } else {
            addressInst.setTon((byte) config.getDestinationAddrTon()); // national ton
            addressInst.setNpi((byte) config.getDestinationAddrNpi()); // numeric plan indicator
            addressInst.setAddress(address, Data.SM_ADDR_LEN);
        }
        return addressInst;
    }

    private ByteBuffer[] prepareMessage(Message message, boolean enablePlayLoad) throws UnsupportedEncodingException {
        ArrayList<String> list = new ArrayList<>();
        String str = message.getMessageText();
        if (message.getMessageId() == null) message.setMessageId((int) getNextUniqIdMessage());
        int maxMessages = config.getMaxMessage();
        int countMessage = 0;

        if (enablePlayLoad && str.length() < 70) {
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
                byteMessage.appendByte(Byte.valueOf(message.getMessageId().toString()));
                byteMessage.appendByte((byte) arr.length);
                byteMessage.appendByte((byte) (i + 1));
                byteMessage.appendString(list.get(i), Data.ENC_UTF16_BE);
                arr[i] = byteMessage;
            }

            return arr;
        }
    }
}


