package net.alfss.smsserver.sms;

import com.google.common.util.concurrent.RateLimiter;
import net.alfss.smsserver.config.GlobalConfig;
import net.alfss.smsserver.database.dao.impl.MessageDAOImpl;
import net.alfss.smsserver.database.entity.Channel;
import net.alfss.smsserver.database.entity.Message;
import net.alfss.smsserver.database.exceptions.DatabaseError;
import net.alfss.smsserver.rabbit.data.ResponseMessage;
import net.alfss.smsserver.rabbit.exceptions.RabbitMqException;
import net.alfss.smsserver.rabbit.exceptions.RabbitMqQueueConnectException;
import net.alfss.smsserver.rabbit.queue.QueueDirectResponse;
import net.alfss.smsserver.sms.exceptions.SmsServerException;
import net.alfss.smsserver.sms.pool.SmsServerConnectPool;
import net.alfss.smsserver.sms.prototype.AsyncSmsServerChild;
import org.smpp.WrongSessionStateException;
import org.smpp.pdu.DeliverSMResp;
import org.smpp.pdu.ValueNotSetException;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: alfss
 * Date: 27.06.14
 * Time: 20:51
 */
public class AsyncSmsServerDeliverResponser extends AsyncSmsServerChild {
    private final MessageDAOImpl messageDAO;
    private final QueueDirectResponse responseQueue;

    public AsyncSmsServerDeliverResponser(GlobalConfig config,
                                          Channel channel,
                                          SmsServerConnectPool connectPool,
                                          RateLimiter rateLimiter,
                                          AtomicInteger seqNumber) {
        super(config, channel, connectPool, rateLimiter, seqNumber);
        this.responseQueue = new QueueDirectResponse(config, channel, true);
        this.messageDAO = new MessageDAOImpl();
    }

    @Override
    public void run() {
        setRunning(true);
        errorMessage("start (channel = " + channel.getName() + ")");
        do {
            try {
                waitResponseMessage();
                rateLimiter.acquire();
            } catch (SmsServerException e) {
                errorMessage("unknow error", e);
                waitRecconectSmpp();
            } catch (RabbitMqQueueConnectException | RabbitMqException e) {
                errorMessage("error ", e);
                waitRecconectRabbitMq(responseQueue);
            } catch (DatabaseError e) {
                errorMessage("error ", e);
                waitRecconectDatabase();
            } catch (NullPointerException e) {
                debugMessage("NullPointerException", e);
            } catch (Exception e) {
                debugMessage("WTF Exception!!! " + channel.getName() + " ", e);
            }
        } while (isRunning() & !isInterrupted());

        errorMessage("Interrupted");
    }

    private void waitResponseMessage() throws InterruptedException {
        Message message = null;
        ResponseMessage responseMessage = responseQueue.getNextMessage();

        debugMessage("read responseMessage " + responseMessage.toString());
        String messageId = responseMessage.getDeliverMessageId();
        int deliverStatus = responseMessage.getDeliverStatus();
        int errorCode = responseMessage.getDeliverErrorCode();

        //get message by message id
        try {
            message = messageDAO.getWaiteDelivery(messageId, channel);
        } catch (IndexOutOfBoundsException e) {
            errorMessage("not found message");
        }

        try {
            session = connectPool.getResource();
            //create response request
            DeliverSMResp deliverSMResp = new DeliverSMResp();
            deliverSMResp.setSequenceNumber(responseMessage.getSequenceNumber());
            //send response
            deliverRespond(deliverSMResp);

            //set status message if exist
            if (!messageId.isEmpty() && deliverStatus == 0) {
                if (message != null) messageDAO.setStatusSuccess(message);
            } else {
                if (message != null) {
                    message.setErrorCode(errorCode);
                    messageDAO.update(message);
                    messageDAO.setStatusFail(message);
                }
            }

            connectPool.returnResource(session);
        } catch (SmsServerException e) {
            try {
                connectPool.returnBrokenResource(session);
            } catch (SmsServerException e1) {
                errorMessage("error Invalidated object and sleep", e);
            } finally {
                responseQueue.purge();
                waitRecconectSmpp();
            }
        }
    }

    private void deliverRespond(DeliverSMResp deliverSMResp) {
        try {
            session.respond(deliverSMResp);
        } catch (ValueNotSetException | IOException | WrongSessionStateException e) {
            errorMessage("error", e);
        }
    }
}
