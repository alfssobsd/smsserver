package net.alfss.smsserver;

import net.alfss.smsserver.config.GlobalConfig;
import net.alfss.smsserver.config.SharedConfig;
import net.alfss.smsserver.http.WebServer;
import net.alfss.smsserver.rabbit.RabbitMQClient;
import net.alfss.smsserver.rabbit.RabbitMQWatcher;
import net.alfss.smsserver.redis.RedisClient;
import net.alfss.smsserver.sms.SmsServerTransmitter;
import org.apache.commons.cli.*;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;

import static java.lang.System.exit;

/**
 * User: alfss
 * Date: 30.09.13
 * Time: 15:20
 */


public class Main implements Daemon {

    private WebServer server;
    private String [] arguments;
    private RedisClient redisClient = null;
    private GlobalConfig globalConfig = null;
    final Logger logger = (Logger) LoggerFactory.getLogger(Main.class);

    private String getVersion() {
        Properties properties = new Properties();
        try {
            properties.load(Main.class.getResourceAsStream("/version.properties"));
            return properties.getProperty("Application.version");
        } catch (IOException e) {
            System.out.println("Exception Occurred" + e.getMessage());
        }
        return "not set version";
    }

    private void startDaemon(String[] args) {

        String configFile = "/etc/smsserver/smsserver.xml";
        String rabbitmqClientMessage = null;

        Options opt = new Options();
        opt.addOption("h", false, "print help");
        opt.addOption("v", false, "print version");
        opt.addOption("c", true, "config file");
        opt.addOption("cr", true, "rabbitmq send message");
        BasicParser parser = new BasicParser();

        try {
            CommandLine cl = parser.parse(opt, args);
            if( cl.hasOption('h') ) {
                HelpFormatter f = new HelpFormatter();
                f.printHelp("OptionsTip", opt);
                exit(0);
            }

            if( cl.hasOption('v')) {
                System.out.println("smsserver: " + getVersion());
                exit(0);
            }

            if (cl.hasOption("cr")) {
                rabbitmqClientMessage = cl.getOptionValue("cr");
            }

            if (cl.hasOption('c')) {
                configFile = cl.getOptionValue("c");
            }

        } catch (ParseException e) {
            System.out.println("Argument Error: " + e.getMessage());
            HelpFormatter f = new HelpFormatter();
            f.printHelp("OptionsTip", opt);
            exit(0);
        }

        logger.error(MessageFormat.format("############# Read config smsserver {0} #############", getVersion()));
        try {
            XMLConfiguration xml_config = new XMLConfiguration(configFile);
            globalConfig = new GlobalConfig(xml_config);
            SharedConfig.setGlobalConfig(globalConfig);
        } catch (ConfigurationException e) {
            logger.error(e.toString());
            exit(0);
        }

        if (rabbitmqClientMessage != null) {
            logger.error(MessageFormat.format("############# RabbitMq send message client {0} #############", rabbitmqClientMessage));
            RabbitMQClient rabbitMQClient = new RabbitMQClient(globalConfig);
            rabbitMQClient.sendMessage(rabbitmqClientMessage);
            exit(0);
        }



        logger.error(MessageFormat.format("############# Start smsserver {0} #############", getVersion()));
        try {
            redisClient = new RedisClient(globalConfig);
            SharedConfig.setRedisClient(redisClient);
            RabbitMQWatcher rabbitMQWatcher = new RabbitMQWatcher(globalConfig, redisClient);
            rabbitMQWatcher.setName("RabbitMQWatcher-Thread");
            rabbitMQWatcher.start();

            for (String channelName: globalConfig.getChannelList()) {
                if (!globalConfig.getChennelConfig(channelName).isFakeChannel()) {
                    SmsServerTransmitter smsServerTransmitter;
                    smsServerTransmitter = new SmsServerTransmitter(globalConfig.getChennelConfig(channelName), redisClient);
                    smsServerTransmitter.setName("smsServerTransmitter-Thread-" + channelName);
                    smsServerTransmitter.start();
                }
            }

            server = new WebServer(globalConfig);
            try {
                server.start();
                server.join();
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        } catch (JedisConnectionException e) {
            logger.error(e.toString());
            exit(0);
        } catch (JedisDataException e) {
            logger.error(e.toString());
            exit(0);
        }

    }

    public static void main(String [] args){
        Main main = new Main();
        main.startDaemon(args);
    }

    @Override
    public void init(DaemonContext daemonContext) throws DaemonInitException, Exception {
        arguments = daemonContext.getArguments();
    }

    @Override
    public void start() throws Exception {
        this.startDaemon(arguments);
    }

    @Override
    public void stop() throws Exception {
        logger.error(MessageFormat.format("############# Stop smsserver {0} #############", getVersion()));
    }

    @Override
    public void destroy() {
    }
}
