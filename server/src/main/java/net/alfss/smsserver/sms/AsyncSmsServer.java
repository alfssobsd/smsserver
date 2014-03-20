package net.alfss.smsserver.sms;

import com.google.common.util.concurrent.RateLimiter;
import net.alfss.smsserver.config.GlobalConfig;
import net.alfss.smsserver.database.dao.impl.ChannelConnectionDAOImpl;
import net.alfss.smsserver.database.entity.Channel;
import net.alfss.smsserver.database.entity.ChannelConnection;
import net.alfss.smsserver.sms.pool.SmsServerConnectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: alfss
 * Date: 25.11.13
 * Time: 14:14
 */
public class AsyncSmsServer extends Thread {

    private final Channel channel;
    public AtomicInteger seqNumber = new AtomicInteger(2);
    final Logger logger = (Logger) LoggerFactory.getLogger(AsyncSmsServer.class);
    private final GlobalConfig config;
    private final RateLimiter rateLimiter;
    private final Object syncObject = new Object();
    private final ArrayList<SmsServerConnectPool> connectPoolList = new ArrayList<>();

    public AsyncSmsServer(GlobalConfig config, Channel channel) {
        this.config = config;
        this.channel = channel;
        this.rateLimiter = RateLimiter.create(channel.getSmppSendMessagePerSecond());
        setThreadName();

        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(1);

        //TODO: падает при отстуствии сервера или проблемах с коннектом!!!
        ChannelConnectionDAOImpl channelConnectionDAO = new ChannelConnectionDAOImpl();
        for (Object obj: channelConnectionDAO.getList(channel)) {
            this.connectPoolList.add(new SmsServerConnectPool(poolConfig, channel, (ChannelConnection) obj));
        }
    }

    @Override
    public void run() {
                          //TODO: нужно сделать пас демо меньше -> demopass
        for (SmsServerConnectPool connectPool : this.connectPoolList) {
            logger.error("AsyncSmsServer: start (channel = " + channel.getName() + ")");
            AsyncSmsServerEnquireLink serverEnquireLink = new AsyncSmsServerEnquireLink(config, channel,
                    connectPool, rateLimiter, seqNumber);
            AsyncSmsServerPrepareMessage prepareMessage = new AsyncSmsServerPrepareMessage(config, channel,
                    connectPool, rateLimiter, seqNumber);
            AsyncSmsServerSubmitter submitter = new AsyncSmsServerSubmitter(config, channel,
                    connectPool, rateLimiter, seqNumber);

            while (true) {
                if(!serverEnquireLink.isRunning())
                    serverEnquireLink.start();

                if (!submitter.isRunning())
                    submitter.start();

                if (!prepareMessage.isRunning())
                    prepareMessage.start();
//


                synchronized (syncObject) {
                    try {
                        syncObject.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void setThreadName() {
        setName(this.getClass().getSimpleName() + '-' + channel.getName());
    }
}
