package net.alfss.smsserver.sms.prototype;

import com.google.common.util.concurrent.RateLimiter;
import net.alfss.smsserver.config.GlobalConfig;
import net.alfss.smsserver.database.entity.Channel;
import net.alfss.smsserver.rabbit.prototype.Queue;
import net.alfss.smsserver.sms.interfaces.AsyncSmsServerThreadInterface;
import net.alfss.smsserver.sms.pool.SmsServerConnectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smpp.Session;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: alfss
 * Date: 27.11.13
 * Time: 17:24
 */
public class AsyncSmsServerChild extends Thread implements AsyncSmsServerThreadInterface {
    protected final GlobalConfig config;
    protected RateLimiter rateLimiter;
    protected AtomicInteger seqNumber;
    protected Session session;
    protected final Channel channel;
    protected final SmsServerConnectPool connectPool;
    protected boolean running = false;
    protected int numberConnection;
    protected final Logger logger = (Logger) LoggerFactory.getLogger(String.valueOf(this.getClass()));

    public AsyncSmsServerChild(GlobalConfig config,
                               Channel channel,
                               SmsServerConnectPool connectPool,
                               int numberConnection,
                               RateLimiter rateLimiter,
                               AtomicInteger seqNumber) {
        this.config = config;
        this.channel = channel;
        this.rateLimiter = rateLimiter;
        this.seqNumber = seqNumber;
        this.connectPool = connectPool;
        this.numberConnection = numberConnection;
        this.session = null;
        setThreadName();

    }

    @Override
    public void run() {
        //need write
    }

    @Override
    public synchronized boolean isRunning() {
        return running;
    }

    @Override
    public synchronized void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void setThreadName() {
        setName(this.getClass().getSimpleName() + '-' + channel.getName() + " connection = " + numberConnection);
    }


    protected void debugMessage(String s, Throwable e) {
        logger.debug("{} : {}", this.getClass().getSimpleName(), s, e);
    }

    protected void debugMessage(String s) {
        logger.debug("{} : {}", this.getClass().getSimpleName(), s);
    }

    protected void errorMessage(String s, Throwable e) {
        logger.error("{} : {}", this.getClass().getSimpleName(), s, e) ;
    }

    protected void errorMessage(String s) {
        logger.error("{} : {}", this.getClass().getSimpleName(), s);
    }

    protected void infoMessage(String s) {
        logger.info(this.getClass().getSimpleName() + ": " + s);
    }

    protected void waitRecconectSmpp() {
        try {
            errorMessage("Error connect to SMPP, recconect to after " + channel.getSmppReconnectTimeOutInMs()/1000 + " seconds");
            sleep(channel.getSmppReconnectTimeOutInMs());
        } catch (InterruptedException e1) {
            errorMessage("error sleep ", e1);
        }
    }

    protected void waitRecconectDatabase() {
        try {
            errorMessage("Error connect to Database, recconect to after " + channel.getSmppReconnectTimeOutInMs()/1000 + " seconds");
            sleep(channel.getSmppReconnectTimeOutInMs());
        } catch (InterruptedException e1) {
            errorMessage("error sleep ", e1);
        }

    }

    protected void waitRecconectRabbitMq(Queue queue) {
        try {
            errorMessage("Error connect to RabbitMQ, recconect to after " + queue.getConnectTimeOutInMs()/1000 + " seconds");
            sleep(queue.getConnectTimeOutInMs());
        } catch (InterruptedException e1) {
            errorMessage("error sleep ", e1);
        }
    }
}
