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
import net.alfss.smsserver.sms.pool.SmsServerConnectPool;
import net.alfss.smsserver.sms.prototype.AsyncSmsServerChild;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: alfss
 * Date: 04.07.14
 * Time: 15:21
 */
public class AsyncSmsServerFakeSubmitter extends AsyncSmsServerChild {
    private final QueueDirectSend sendQueue;
    private final MessageDAOImpl messageDAO;


    public AsyncSmsServerFakeSubmitter(GlobalConfig config, Channel channel, SmsServerConnectPool connectPool, RateLimiter rateLimiter, AtomicInteger seqNumber) {
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
        if (message != null) {
            message.setSequenceNumber(seqNumber.incrementAndGet());
            message.setMessageSmsId(message.getSequenceNumber() + "-fake");
            messageDAO.update(message);
            messageDAO.setStatusWaitResponse(message);
            messageDAO.setStatusSuccess(message);
        }
    }
}
