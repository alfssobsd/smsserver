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
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: alfss
 * Date: 25.11.13
 * Time: 14:14
 */
public class SmsServerMaster extends Thread {

    private final Channel channel;
    public AtomicInteger seqNumber = new AtomicInteger(2);
    final Logger logger = (Logger) LoggerFactory.getLogger(SmsServerMaster.class);
    private final GlobalConfig config;
    private final RateLimiter rateLimiter;
    private final Object syncObject = new Object();
    private final ArrayList<SmsServerConnectPool> connectPoolList = new ArrayList<>();
    private final ArrayList<SmsServer> smsServerStartersList = new ArrayList<>();

    public SmsServerMaster(GlobalConfig config, Channel channel) {
        this.config = config;
        this.channel = channel;
        this.rateLimiter = RateLimiter.create(channel.getSmppMaxMessagePerSecond());
        setThreadName();

        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(1);

        ChannelConnectionDAOImpl channelConnectionDAO = new ChannelConnectionDAOImpl();
        for (ListIterator iterator = channelConnectionDAO.getList(channel).listIterator(); iterator.hasNext();) {
            int numberConnection = iterator.nextIndex() + 1;
            ChannelConnection obj  = (ChannelConnection) iterator.next();
            this.connectPoolList.add(new SmsServerConnectPool(poolConfig, config, channel, obj, numberConnection));
        }
    }

    //TODO: нужно сохранять укозатели на запушенные smsServerStarter и нужен методо для рестарта
    //TODO: требуется управление из вне, рестар и остановка
    @Override
    public void run() {

        createCildren();
        startChildren();
        synchronized (syncObject) {
            try {
                syncObject.wait();
            } catch (InterruptedException e) {
                logger.debug("Interrupted");
                stopChildren();
            }
        }

        logger.error("stop");
    }

    private void startChildren() {
        synchronized (smsServerStartersList) {
            for (SmsServer smsServerStarter:smsServerStartersList) {
                smsServerStarter.start();
            }
        }
    }

    private void stopChildren() {
        synchronized (smsServerStartersList) {
            for (SmsServer smsServerStarter:smsServerStartersList) {
                smsServerStarter.interrupt();
            }
        }
    }

    private void addChildToList(SmsServer smsServerStarter) {
        synchronized (smsServerStartersList) {
            smsServerStartersList.add(smsServerStarter);
        }
    }

    private void createCildren() {
        for (ListIterator<SmsServerConnectPool> iterator = this.connectPoolList.listIterator(); iterator.hasNext(); ) {
            int numberConnection = iterator.nextIndex() + 1;
            SmsServerConnectPool connectPool = iterator.next();
            logger.error("AsyncSmsServer(connection = " + numberConnection + "): start (channel = " + channel.getName() + ")");
            SmsServer smsServerStarter = new SmsServer(config, channel, connectPool, numberConnection, rateLimiter, seqNumber);
            addChildToList(smsServerStarter);
        }
    }

    public void setThreadName() {
        setName(this.getClass().getSimpleName() + '-' + channel.getName());
    }
}
