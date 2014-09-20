package net.alfss.smsserver.sms;

import com.google.common.util.concurrent.RateLimiter;
import net.alfss.smsserver.config.GlobalConfig;
import net.alfss.smsserver.database.entity.Channel;
import net.alfss.smsserver.sms.pool.SmsServerConnectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: alfss
 * Date: 17.09.14
 * Time: 14:14
 */
public class SmsServer extends Thread {
    final Logger logger = (Logger) LoggerFactory.getLogger(SmsServer.class);
    private final Object syncObject = new Object();
    private final Channel channel;
    private final SmsServerConnectPool connectPool;
    private final int numberConnection;


    private final SmsServerEnquireLink enquireLink;
    private final SmsServerPrepareMessage prepareMessage;
    private final SmsServerSubmitter submitter;
    private final SmsServerFakeSubmitter fakeSubmitter;
    private final SmsServerDeliverResponser deliverResponser;

    public SmsServer(GlobalConfig config,
                     Channel channel,
                     SmsServerConnectPool connectPool,
                     int numberConnection,
                     RateLimiter rateLimiter,
                     AtomicInteger seqNumber) {

        this.channel = channel;
        this.connectPool = connectPool;
        this.numberConnection = numberConnection;
        setThreadName();

        enquireLink = new SmsServerEnquireLink(config, channel, connectPool, numberConnection, rateLimiter, seqNumber);
        prepareMessage = new SmsServerPrepareMessage(config, channel, connectPool, numberConnection, rateLimiter, seqNumber);
        submitter = new SmsServerSubmitter(config, channel, connectPool, numberConnection, rateLimiter, seqNumber);
        fakeSubmitter = new SmsServerFakeSubmitter(config, channel, connectPool, numberConnection,  rateLimiter, seqNumber);
        deliverResponser = new SmsServerDeliverResponser(config, channel, connectPool, numberConnection, rateLimiter, seqNumber);
    }

    @Override
    public void run() {
      startAllChildren();
      synchronized (syncObject) {
          try {
              syncObject.wait();
          } catch (InterruptedException e) {
              stopAllChildren();
              logger.debug("Interrupted", e);
          }
      }
      logger.debug("stop");
    }

    private void startAllChildren() {
        logger.debug("start all children");
        if (!prepareMessage.isRunning()) prepareMessage.start();

        if (channel.isFake()) {
            if (!fakeSubmitter.isRunning()) fakeSubmitter.start();
        } else {
            if (!deliverResponser.isRunning()) deliverResponser.start();
            if (!enquireLink.isRunning()) enquireLink.start();
            if (!submitter.isRunning()) submitter.start();
        }
    }

    private void stopAllChildren() {
        logger.debug("stop all children");
        if (prepareMessage.isRunning()) prepareMessage.interrupt();

        if (channel.isFake()) {
            if (fakeSubmitter.isRunning()) fakeSubmitter.interrupt();
        } else {
            if (deliverResponser.isRunning()) deliverResponser.interrupt();
            if (enquireLink.isRunning()) enquireLink.interrupt();
            if (submitter.isRunning()) submitter.interrupt();
        }
    }

    public void setThreadName() {
        setName(this.getClass().getSimpleName() + '-' + channel.getName() + " connection = " + numberConnection);
    }
}
