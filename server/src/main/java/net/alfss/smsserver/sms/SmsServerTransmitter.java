package net.alfss.smsserver.sms;

/**
 * User: alfss
 * Date: 03.10.13
 * Time: 16:26
 */
//public class SmsServerTransmitter extends Thread {
//
//    private SmsAddressUtils smsAddressUtils;
//    private SmsMessageUtils smsMessageUtils;
//    private BindRequest bindRequest;
//    private Session session;
//    private int seqNumber = 2;
//    private byte uniqIdMessage = (byte) 0x00;
//    private boolean isConnect = false;
//    final Logger logger = (Logger) LoggerFactory.getLogger(SmsServerTransmitter.class);
//    private final ChannelConfig config;
//    private final RedisClient redisClient;
//    private RateLimiter rateLimiter;
//    private SuccessSend successSendLogger;
//    private FailSend failSendLogger;
//
//    public SmsServerTransmitter(ChannelConfig config, RedisClient redisClient) {
//        this.config = config;
//        this.redisClient = redisClient;
//        rateLimiter = RateLimiter.create(config.getMessagePerScond());
//        bindRequest = new BindTransmitter();
//        this.smsMessageUtils = new SmsMessageUtils(config);
//
//        this.successSendLogger = new SuccessSend();
//        this.failSendLogger = new FailSend();
//    }
//
//    @Override
//    public void run() {
//        logger.error("SmsServerTransmitter: Start (channel = " + config.getChannel() + ")");
//        waitMessage();
//    }
//
//    private void waitMessage() {
//        while (!isConnect) {
//            try {
//                this.connect();
//            } catch (SmsServerConnectingErrorException e) {
//                logger.error("SmsServerTransmitter: init wait message error (channel = " +
//                    config.getChannel() + ") " + e.toString());
//                try {
//                    sleep(1000 * config.getReconnectTimeOut());
//                } catch (InterruptedException e1) {
//                    logger.error("SmsServerTransmitter: " + e1.toString());
//                }
//            } catch (Exception e) {
//                logger.error("SmsServerTransmitter: waitMessage connect unknown error !!!!! (channel = " +
//                        config.getChannel() + ") " + e.toString());
//            }
//        }
//
//        while (true) {
//            logger.debug("SmsServerTransmitter: wait message (channel = " + config.getChannel() + ")");
//            Message message = null;
//            try {
//                message = redisClient.popBlockMessageFromList(config.getEnquireLinkInterval(), config.getChannelQueue());
//                logger.debug("SmsServerTransmitter: get message (channel = " + config.getChannel() + ")");
//                if (message != null) {
//                    rateLimiter.acquire();
//                    if (message.isExpired()) {
//                        throw new SmsServerMessageError("message expire");
//                    }
//                    sendMessage(message);
//                } else {
//                    enquireLinkRequest();
//                }
//            } catch (SmsServerConnectingErrorException e) {
//                logger.error("SmsServerTransmitter: connecting error (channel = " +
//                        config.getChannel() + ") " + e.toString());
//                returnMessagToList(message);
//                reconnect();
//            } catch (SmsServerNeedWait e) {
//                logger.error("SmsServerTransmitter: need wait (channel = " +
//                        config.getChannel() + ") " + e.toString());
//                returnMessagToList(message);
//                try {
//                    sleep(config.getWaitResendTimeOut() * 1000);
//                } catch (InterruptedException e1) {
//                    e1.printStackTrace();
//                }
//            } catch (SmsServerMessageError e) {
//                logger.error("SmsServerTransmitter: error message (channel = " +
//                        config.getChannel() + ") " + e.toString());
//                failSendLogger.writeLog(message);
//            } catch (JedisConnectionException e) {
//                logger.error("SmsServerTransmitter: error connect to redis (channel = " +
//                        config.getChannel() + ") " + e.toString());
//                try {
//                    sleep(redisClient.getConnectTimeOutInMs());
//                } catch (InterruptedException e1) {
//                    e1.printStackTrace();
//                }
//            } catch (JedisDataException e) {
//                logger.error("SmsServerTransmitter: error read data from redis (channel = " +
//                        config.getChannel() + ") " + e.toString());
//            } catch (RedisUnknownError e) {
//                logger.error("SmsServerTransmitter: unknown error redis (channel = " +
//                        config.getChannel() + ") " + e.toString());
//            } catch (Exception e) {
//                logger.error("SmsServerTransmitter: waitMessage unknown error !!!!!(channel = " +
//                        config.getChannel() + ") " + e.toString());
//            }
//        }
//    }
//
//    private void connect() throws SmsServerConnectingErrorException {
//        if (!isConnect) {
//
//            Connection conn = new TCPIPConnection(config.getHost(), config.getSendPort());
//            session = new Session(conn);
//
//            try {
//                bindRequest.setSystemId(config.getUser());
//            } catch (WrongLengthOfStringException e) {
//                logger.error("SmsServerTransmitter: connect user wrong length (channel = " +
//                        config.getChannel() + ") " + e.toString());
//                throw new SmsServerConnectingErrorException("Error connect");
//            }
//            try {
//                bindRequest.setPassword(config.getPassword());
//            } catch (WrongLengthOfStringException e) {
//                logger.error("SmsServerTransmitter: connect password wrong length (channel = " +
//                        config.getChannel() + ") " + e.toString());
//                throw new SmsServerConnectingErrorException("Error connect");
//            }
//            try {
//                bindRequest.setSystemType(config.getSystemType());
//            } catch (WrongLengthOfStringException e) {
//                logger.error("SmsServerTransmitter: connect system-type wrong length (channel = " +
//                        config.getChannel() + ") " + e.toString());
//                throw new SmsServerConnectingErrorException("Error connect");
//            }
//
//            bindRequest.setAddressRange(config.getAddressRange());
//            try {
//                Response response = session.bind(bindRequest);
//                if(response.getCommandStatus() == Data.ESME_ROK) {
//                    logger.error("SmsServerTransmitter: connected to " +
//                            config.getHost() + " (channel =  " + config.getChannel() + ")");
//                    isConnect = true;
//                }
//            } catch (TimeoutException | PDUException | IOException | WrongSessionStateException e) {
//                logger.error("SmsServerTransmitter: connect (channel = " +
//                        config.getChannel() + ") " + e.toString());
//            }
//
//            if(!isConnect) {
//                throw new SmsServerConnectingErrorException("Error connect");
//            }
//        }
//    }
//
//    private void disconnect() {
//        try {
//            if(isConnect) {
//                isConnect = false;
//                session.unbind();
//            }
//        } catch (TimeoutException | PDUException | IOException | WrongSessionStateException e) {
//            logger.error("SmsServerTransmitter: disconnect (channel = " +
//                    config.getChannel() + ") " + e.toString());
//        } catch (Exception e) {
//            logger.error("SmsServerTransmitter: disconnect unknown error !!!!! (channel = " +
//                    config.getChannel() + ") " + e.toString());
//        }
//    }
//
//    private void reconnect() {
//        if(isConnect) {
//            disconnect();
//        }
//        try {
//            logger.error("SmsServerTransmitter: reconnect after " +
//                    config.getReconnectTimeOut() + " seconds (channel =  " + config.getChannel() + ")");
//            sleep(1000 * config.getReconnectTimeOut());
//            connect();
//        } catch (InterruptedException | SmsServerConnectingErrorException e) {
//            logger.error("SmsServerTransmitter: recconect error (channel = " +
//                    config.getChannel() + ") " + e.toString());
//        } catch (Exception e) {
//            logger.error("SmsServerTransmitter: recconect unknown error !!!!! (channel = " +
//                    config.getChannel() + ") " + e.toString());
//        }
//    }
//
//    private void returnMessagToList(Message message) {
//        if (message != null)  {
//            try {
//                redisClient.pushMessageToHeadList(message);
//            } catch (NullPointerException e1) {
//                logger.error("SmsServerTransmitter: connecting error -> error push to redis (channel = " +
//                        config.getChannel() + ") message =  " +  message.toString() + " " + e1.toString());
//                failSendLogger.writeLog(message);
//            } catch (RedisUnknownError e1) {
//                logger.error("SmsServerTransmitter: unknown error redis (channel = " +
//                        config.getChannel() + ") message =  " + message.toString() + " " + e1.toString());
//                failSendLogger.writeLog(message);
//            }
//        }
//    }
//
//    private void enquireLinkRequest() throws SmsServerConnectingErrorException {
//        EnquireLink request = new EnquireLink();
//        request.setSequenceNumber(this.getNextSeqNumber());
//        try {
//            EnquireLinkResp response = session.enquireLink(request);
//            if(response.getCommandStatus() != Data.ESME_ROK) {
//                logger.error("SmsServerTransmitter: enquireLinkRequest error messageStatus (channel =  " +
//                        config.getChannel() + ") messageStatus = " + response.getCommandStatus());
//                throw new SmsServerConnectingErrorException("Error enquireLink");
//            }
//            logger.debug("SmsServerTransmitter: enquireLinkRequest success (channel =  " +
//                    config.getChannel() + ") messageStatus = " + response.getCommandStatus());
//        } catch (TimeoutException | PDUException | IOException | WrongSessionStateException | NullPointerException e) {
//            logger.error("SmsServerTransmitter: enquireLinkRequest (channel =  " +
//                    config.getChannel() + ") " + e.toString());
//            throw new SmsServerConnectingErrorException("Error enquireLink");
//        }
//
//    }
//
//    private SubmitSM createSubmitRequest(Message message, byte esmClass) throws SmsServerConnectingErrorException, SmsServerMessageError {
//
//        SubmitSM request = new SubmitSM();
//        request.setDataCoding((byte) 0x08);
//        try {
//            if (message.getSourceAddress() != null) {
//                request.setSourceAddr(message.getSourceAddress());
//            } else {
//                request.setSourceAddr(smsAddressUtils.createAddress(null, config));
//            }
//        } catch (WrongLengthOfStringException e) {
//            throw new SmsServerConnectingErrorException("source address " + config.getSoureceAddress() + "," + e.toString());
//        }
//
//        try {
//            request.setDestAddr(smsAddressUtils.createAddress(message.getDestinationAddress(), config));
//        } catch (WrongLengthOfStringException e) {
//            throw new SmsServerMessageError("destination address " + message.getDestinationAddress() + "," + e.toString());
//        }
//        request.setSequenceNumber(this.getNextSeqNumber());
//        request.setEsmClass(esmClass);
//        request.setRegisteredDelivery(Data.SM_SMSC_RECEIPT_REQUESTED);
//
//        return request;
//    }
//
//    private void runSubmitRequest(SubmitSM request) throws PDUException, TimeoutException,
//            WrongSessionStateException, IOException, SmsServerConnectingErrorException, SmsServerNeedWait {
//        try {
//            SubmitSMResp response = session.submit(request);
//            //TODO:get message id
//            //response.getReferenceNumber();
//            switch (response.getCommandStatus()) {
//                case Data.ESME_ROK:
//                    logger.debug("SmsServerTransmitter: sendMessage messageId = submission submitted  (channel =  " +
//                            config.getChannel() + ") messageStatus = " + response.getCommandStatus());
//                    break;
//                case Data.ESME_RMSGQFUL:
//                    logger.error("SmsServerTransmitter: smpp server queue is full  (channel =  " +
//                            config.getChannel() + ") messageStatus = " + response.getCommandStatus());
//                    throw new SmsServerNeedWait("submit ESME_RMSGQFUL");
//                default:
//                    logger.error("SmsServerTransmitter: sendMessage submission failed  (channel =  " +
//                            config.getChannel() + ") messageStatus = " + response.getCommandStatus());
//                    throw new SmsServerConnectingErrorException("submit unknown error");
//            }
//        } catch (NullPointerException e) {
//            throw new SmsServerConnectingErrorException("WTF null pointer submit requrest " + e.toString());
//        }
//    }
//
//
//    private boolean sendMessage(Message message) throws SmsServerConnectingErrorException, SmsServerMessageError, SmsServerNeedWait {
//
//        byte esmClass = (byte) 0x00;
//
//        ByteBuffer[] preparedMessage;
//        try {
//            preparedMessage = nul
//        } catch (UnsupportedEncodingException | NullPointerException e) {
//            throw new SmsServerMessageError("smsMessageUtils " + e.toString());
//        }
//
//        if (preparedMessage.length > 1) {
//            esmClass = (byte) 0x40;
//        }
//
//        try {
//            if (config.getEnablePayLoad()) {
//                SubmitSM request = createSubmitRequest(message, esmClass);
//                request.setMessagePayload(preparedMessage[0]);
//                runSubmitRequest(request);
//            } else {
//                if (preparedMessage.length > 1) {
//                    for (int i = message.getStartPart(); i < preparedMessage.length; i++) {
//                        message.setStartPart(i);
//                        SubmitSM request = createSubmitRequest(message, esmClass);
//                        //TODO: разобраться с TLV
//                        if ((i + 1) < preparedMessage.length) {
//                            try {
//                                request.setExtraOptional((short) 1062, new ByteBuffer(new byte[]{(byte) 0x01}));
//                            } catch (TLVException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        request.setShortMessageData(preparedMessage[i]);
//                        runSubmitRequest(request);
//                    }
//                } else {
//                    SubmitSM request = createSubmitRequest(message, esmClass);
//                    request.setShortMessageData(preparedMessage[0]);
//                    runSubmitRequest(request);
//                }
//
//            }
//        } catch (TimeoutException | PDUException | IOException | WrongSessionStateException| NullPointerException e) {
//            throw new SmsServerConnectingErrorException(e.toString());
//        } catch (NotEnoughDataInByteBufferException | TerminatingZeroNotFoundException e) {
//            throw new SmsServerMessageError(e.toString());
//        }
//
//        successSendLogger.writeLog(message);
//
//        return  true;
//    }
//
//
//    private int getNextSeqNumber() {
//        return seqNumber++;
//    }
//
//}


