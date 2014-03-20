package net.alfss.smsserver.sms;

import net.alfss.smsserver.database.dao.impl.MessageDAOImpl;
import net.alfss.smsserver.database.entity.Channel;
import net.alfss.smsserver.database.entity.Message;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: alfss
 * Date: 16.11.13
 * Time: 7:15
 */
public class AsyncSmsServerEventListener extends SmppObject implements ServerPDUEventListener {

    private final Channel channel;
    private final MessageDAOImpl messageDAO;
    Pattern deliverMessageId = Pattern.compile("^id:(.*) sub:");
    Pattern deliverStat = Pattern.compile("stat:([A-Z]+)");
    Pattern deliverError = Pattern.compile("err:([0-9]+)");

    final Logger logger = (Logger) LoggerFactory.getLogger(AsyncSmsServerEventListener.class);

    public AsyncSmsServerEventListener(Channel channel) {
        this.channel = channel;
        this.messageDAO = new MessageDAOImpl();
    }

    @Override
    public void handleEvent(ServerPDUEvent event) {
        try {
            PDU pdu = event.getPDU();
            int commandId = pdu.getCommandId();

            if (pdu.isRequest()) {
                if (commandId == Data.DELIVER_SM) {
                    handlerDeliver((DeliverSM) pdu);
                } else {
                    logger.error(channel.getName() + ":async request received not implement" + pdu.debugString());
                }
            } else if (pdu.isResponse()) {
                if (pdu.getClass() == EnquireLinkResp.class) {
                    handlerEnquireLinkResp((EnquireLinkResp) pdu);
                } else if (pdu.getClass() == SubmitSMResp.class) {
                    handlerSubmitSMResp((SubmitSMResp) pdu);
                } else {
                    logger.error(channel.getName() + ":async response received " + pdu.debugString());
                }
            } else {
                logger.error(channel.getName() + ":pdu of unknown class (not request nor " + "response)" +
                        " received, discarding " + pdu.debugString());
            }
        } catch (Exception e) {
            logger.debug(channel.getName(), e);
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
        logger.debug("AsyncSmsServerEventListener: EnquireLinkResp channel = " + channel.getName() + " received " + response.debugString());
        if(response.getCommandStatus() != Data.ESME_ROK) {
            logger.error("SmsServerTransmitter: enquireLinkRequest error status (channel =  " +
                    channel.getName() + ") status = " + response.getCommandStatus());
//            throw new SmsServerConnectionException("AsyncSmsServerEventListener: Error enquireLink");
        }
        logger.debug("SmsServerTransmitter: enquireLinkRequest success (channel =  " +
                channel.getName() + ") status = " + response.getCommandStatus());

    }


    private void handlerSubmitSMResp(SubmitSMResp response) {
        logger.error("messageId:" + response.getMessageId());
        logger.error("SequenceNumber:" + response.getSequenceNumber());

        Message message = messageDAO.getWaitResponse(response.getSequenceNumber(), channel);
        switch (response.getCommandStatus()) {

            case Data.ESME_ROK:
                message.setMessageSmsId(response.getMessageId());
                messageDAO.setStatusWaiteDelivery(message);
                logger.debug("SmsServerTransmitter: sendMessage messageId = submission submitted  (channel =  " +
                        channel.getName() + ") status = " + response.getCommandStatus());
                break;
            case Data.ESME_RMSGQFUL:
                logger.error("SmsServerTransmitter: smpp server queue is full  (channel =  " +
                        channel.getName() + ") status = " + response.getCommandStatus());
                messageDAO.setStatusFail(message);
//                throw new SmsServerConnectionException("AsyncSmsServerEventListener: submit ESME_RMSGQFUL");
            default:
                logger.error("SmsServerTransmitter: sendMessage submission failed  (channel =  " +
                        channel.getName() + ") status = " + response.getCommandStatus());
                messageDAO.setStatusFail(message);
//                throw new SmsServerConnectionException("AsyncSmsServerEventListener: submit ESME_RMSGQFUL");
        }


    }


    private void handlerDeliver(DeliverSM deliverSM) {
        logger.error(channel.getName() + ":async deliverSM" + deliverSM.debugString());
        logger.error("msg_id: " +  getDeliverMessageId(deliverSM.getShortMessage()));
        logger.error("msg_stat: " +  getDeliverStat(deliverSM.getShortMessage()));
        logger.error("msg_err_code: " +  getDeliverErrorCode(deliverSM.getShortMessage()));
    }


    private String getDeliverMessageId(String shortMessage) {
        Matcher matcher = deliverMessageId.matcher(shortMessage);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }


    private int getDeliverErrorCode(String shortMessage) {
        Matcher matcher = deliverError.matcher(shortMessage);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        } else {
            return 0;
        }
    }
    //Вернется статус UNDELIV и код ошибки, подробнее здесь http://smsc.ru/api/smpp/#errs
    //251	Превышен лимит на один номер.	Превышен суточный лимит сообщений на один номер. Лимит устанавливается Клиентом в личном кабинете в пункте "Настройки". Также такая ошибка возможна при отправке более 50 сообщений одному абоненту, которые были отправлены с перерывом между сообщениями менее 30 секунд.
    private int getDeliverStat(String shortMessage) {
        Matcher matcher = deliverStat.matcher(shortMessage);
        if (matcher.find()) {
            switch (matcher.group(1)) {
                case "DELIVRD":
                    return 0;
                case "EXPIRED":
                    return 1;
                case "UNDELIV":
                    return 2;
                case "REJECTD":
                    return 3;
            }
        }

        return 3;
    }


}



