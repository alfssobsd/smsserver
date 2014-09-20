package net.alfss.smsserver;

import net.alfss.smsserver.config.GlobalConfig;
import net.alfss.smsserver.config.SharedConfig;
import net.alfss.smsserver.database.dao.impl.ChannelDAOImpl;
import net.alfss.smsserver.database.entity.Channel;
import net.alfss.smsserver.http.WebServer;
import net.alfss.smsserver.sms.SmsServerMaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * User: alfss
 * Date: 19.09.14
 * Time: 17:19
 */
public class Master extends Thread {

    private final GlobalConfig config;
    private final Object syncObject = new Object();
    final Logger logger = (Logger) LoggerFactory.getLogger(Master.class);
    private static final Map<Integer,SmsServerMaster> smsServerList = new HashMap<>();
    private WebServer webServer;

    public Master(GlobalConfig config) {
        SharedConfig.setGlobalConfig(config);
        this.config = config;
    }

    @Override
    public void run() {
        startAllThread();
        synchronized (syncObject) {
            try {
                syncObject.wait();
            } catch (InterruptedException e) {
                logger.debug("Interrupted");
            }
        }
        stopAllThread();
    }

    private void startAllThread() {
        //start web server
        webServer = new WebServer(config);
        try {
            webServer.start();
            logger.debug("start web server");
        } catch (Exception e) {
            e.printStackTrace();
        }


        logger.debug("start sms channel");
        //start enable sms channel
        ChannelDAOImpl channelDAO = new ChannelDAOImpl();
        for (Object obj: channelDAO.getAllEnableList()) {
            startSmsServer((Channel) obj);
        }
    }

    private void stopAllThread() {
        try {
            webServer.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //STOP sms servers
    }


    public static void startSmsServer(Channel channel) {
        synchronized (smsServerList) {
            SmsServerMaster server = new SmsServerMaster(SharedConfig.getGlobalConfig(), channel);
            smsServerList.put(channel.getChannelId(), server);
            server.start();
        }
    }

    public static void stopSmsServer(Channel channel) {
        synchronized (smsServerList) {
            SmsServerMaster server = smsServerList.get(channel.getChannelId());
            smsServerList.remove(channel.getChannelId());
            server.interrupt();
        }
    }
}
