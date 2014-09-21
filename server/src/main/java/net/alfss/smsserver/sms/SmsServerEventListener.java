package net.alfss.smsserver.sms;

import net.alfss.smsserver.config.GlobalConfig;
import net.alfss.smsserver.database.dao.impl.MessageDAOImpl;
import net.alfss.smsserver.database.entity.Channel;
import net.alfss.smsserver.database.entity.Message;
import net.alfss.smsserver.rabbit.data.ResponseMessage;
import net.alfss.smsserver.rabbit.exceptions.RabbitMqQueueConnectException;
import net.alfss.smsserver.rabbit.queue.QueueDirectResponse;
import net.alfss.smsserver.sms.exceptions.SmsServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smpp.Data;
import org.smpp.ServerPDUEvent;
import org.smpp.ServerPDUEventListener;
import org.smpp.SmppObject;
import org.smpp.pdu.DeliverSM;
import org.smpp.pdu.EnquireLinkResp;
import org.smpp.pdu.PDU;
import org.smpp.pdu.SubmitSMResp;

/**
 * User: alfss
 * Date: 16.11.13
 * Time: 7:15
 */
public class SmsServerEventListener extends SmppObject implements ServerPDUEventListener {

    private final Channel channel;
    private final QueueDirectResponse responseQueue;
    private final MessageDAOImpl messageDAO;
    private final String loggerDescriptionName;

    final Logger logger = (Logger) LoggerFactory.getLogger(SmsServerEventListener.class);


    //TODO: убедится что все ошибки обрабатываются.
    public SmsServerEventListener(GlobalConfig config, Channel channel, int numberConnection) {
        this.channel = channel;
        this.messageDAO = new MessageDAOImpl();
        this.responseQueue = new QueueDirectResponse(config, channel, false);
        loggerDescriptionName = this.getClass().getSimpleName() + '-' + channel.getName() + " connection = " + numberConnection;
    }

    @Override
    public void handleEvent(ServerPDUEvent event) {
        try {
            PDU pdu = event.getPDU();
            int commandId = pdu.getCommandId();

            if (pdu.isRequest()) {
                if (commandId == Data.DELIVER_SM) {
                    handlerDeliver((DeliverSM) pdu, pdu);
                } else {
                    errorMessage(channel.getName() + ":async request received not implement" + pdu.debugString());
                }
            } else if (pdu.isResponse()) {
                if (pdu.getClass() == EnquireLinkResp.class) {
                    handlerEnquireLinkResp((EnquireLinkResp) pdu);
                } else if (pdu.getClass() == SubmitSMResp.class) {
                    handlerSubmitSMResp((SubmitSMResp) pdu);
                } else {
                    debugMessage(channel.getName() + ":async response received " + pdu.debugString());
                }
            } else {
                errorMessage(channel.getName() + ":pdu of unknown class (not request nor " + "response)" +
                        " received, discarding " + pdu.debugString());
            }
        } catch (Exception e) {
            debugMessage(channel.getName(), e);
        }
    }

//    public ServerPDUEvent getRequestEvent(long timeout) {
//        ServerPDUEvent pduEvent = null;
//        synchronized (requestEvents) {
//            if (requestEvents.isEmpty()) {
//                try {
//                    requestEvents.wait(timeout);
//                } catch (InterruptedException e) {
//                    // ignoring, actually this is what we're waiting for
//                }
//            }
//            if (!requestEvents.isEmpty()) {
//                pduEvent = (ServerPDUEvent) requestEvents.dequeue();
//            }
//        }
//        return pduEvent;
//    }


    private void handlerEnquireLinkResp(EnquireLinkResp response) {
        debugMessage("AsyncSmsServerEventListener: EnquireLinkResp channel = " + channel.getName() + " received " + response.debugString());
        if(response.getCommandStatus() != Data.ESME_ROK) {
            errorMessage("SmsServerTransmitter: enquireLinkRequest error messageStatus (channel =  " +
                    channel.getName() + ") messageStatus = " + response.getCommandStatus());
//            throw new SmsServerConnectionException("AsyncSmsServerEventListener: Error enquireLink");
        }
        debugMessage("SmsServerTransmitter: enquireLinkRequest success (channel =  " +
                channel.getName() + ") messageStatus = " + response.getCommandStatus());

    }


    private void handlerSubmitSMResp(SubmitSMResp response) {
        debugMessage("messageId:" + response.getMessageId());
        debugMessage("SequenceNumber:" + response.getSequenceNumber());

        Message message = messageDAO.getWaitResponse(response.getSequenceNumber(), channel);
        switch (response.getCommandStatus()) {

            case Data.ESME_ROK:
                message.setMessageSmsId(response.getMessageId());
                messageDAO.setStatusWaiteDelivery(message);
                debugMessage("SmsServerTransmitter: sendMessage messageId = submission submitted  (channel =  " +
                        channel.getName() + ") messageStatus = " + response.getCommandStatus());
                break;
            case Data.ESME_RMSGQFUL:
                errorMessage("SmsServerTransmitter: smpp server queue is full  (channel =  " +
                        channel.getName() + ") messageStatus = " + response.getCommandStatus());
                messageDAO.setStatusFail(message);
//                throw new SmsServerConnectionException("AsyncSmsServerEventListener: submit ESME_RMSGQFUL");
            default:
                errorMessage("SmsServerTransmitter: sendMessage submission failed  (channel =  " +
                        channel.getName() + ") messageStatus = " + response.getCommandStatus());
                messageDAO.setStatusFail(message);
//                throw new SmsServerConnectionException("AsyncSmsServerEventListener: submit ESME_RMSGQFUL");
        }


    }

//TODO: разобраться с InterruptedException
    private void handlerDeliver(DeliverSM deliverSM, PDU pdu) throws InterruptedException {
        debugMessage(channel.getName() + ":async deliverSM" + deliverSM.debugString());
        try {
            responseQueue.publish(new ResponseMessage(deliverSM.getShortMessage(), pdu.getSequenceNumber()));
        } catch (SmsServerException | RabbitMqQueueConnectException e) {
            errorMessage("Error connect to RabbitMQ ");
        }
    }

    protected void debugMessage(String s, Throwable e) {
        logger.debug("[ {} ]: {}", loggerDescriptionName, s, e);
    }

    protected void debugMessage(String s) {
        logger.debug("[ {} ]: {}", loggerDescriptionName, s);
    }

    protected void errorMessage(String s, Throwable e) {
        logger.error("[ {} ] : {}", loggerDescriptionName, s, e) ;
    }

    protected void errorMessage(String s) {
        logger.error("[ {} ] : {}", loggerDescriptionName, s);
    }

    protected void infoMessage(String s) {
        logger.info("[ {} ] : {}", loggerDescriptionName, s);
    }
}



