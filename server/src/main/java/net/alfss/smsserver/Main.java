package net.alfss.smsserver;

import com.googlecode.flyway.core.Flyway;
import net.alfss.smsserver.config.GlobalConfig;
import net.alfss.smsserver.config.SharedConfig;
import net.alfss.smsserver.database.dao.impl.ChannelDAOImpl;
import net.alfss.smsserver.database.entity.Channel;
import net.alfss.smsserver.http.WebServer;
import net.alfss.smsserver.redis.RedisClient;
import net.alfss.smsserver.sms.AsyncSmsServer;
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
//            RabbitMQClient rabbitMQClient = new RabbitMQClient(globalConfig);
//            rabbitMQClient.sendMessage(rabbitmqClientMessage);
            exit(0);
        }



        logger.error(MessageFormat.format("############# Start smsserver {0} #############", getVersion()));
        try {

            Flyway flyway = new Flyway();
            flyway.setDataSource(globalConfig.getDbUrl(), globalConfig.getDbUserName(), globalConfig.getDbPassword());
            flyway.migrate();
//            MessageDAOImpl messageDAO = new MessageDAOImpl();
//////            messageDAO.get(1);
////
//            ChannelDAOImpl channelDAO = new ChannelDAOImpl();
//            Channel channel = channelDAO.get(1);
////
////            for (int i = 0; i < 150; i++) {
//                Message message = new Message();
//                String test = "Да и это не просто обычный словарь. Там используется русская морфология для лучшего поиска синонимов. Дополнив большим числом словоупотреблений из русскоязычных текстов можно значительно улучшить качество поиска, а так же отфильтровать далеко не идеальную базу слов.";
//                message.setTo("79062783751");
//                message.setMessageData(test.getBytes());
//                message.setSequenceNumber(1);
//                message.setPayload(false);
//                messageDAO.create(message, channel);
//            }

//            ChannelDAOImpl channelDAO1 = new ChannelDAOImpl();
//            Channel channel1 = channelDAO1.get(1);
//            messageDAO.getList(channel, 100, 0);
//            <channel>smscru</channel>
//            <channel-queue>smscru</channel-queue>
//            <host>localhost</host>
//            <send-port>2775</send-port>
//            <receive-port>2775</receive-port>
//            <bind-mode>t</bind-mode>
//            <enable-payload>false</enable-payload>
//            <address-range>0006543</address-range>
//            <username>smppclient1</username>
//            <password>password</password>
//            <source-addr>0006543</source-addr>
//            <source-addr-ton>5</source-addr-ton>
//            <source-addr-npi>1</source-addr-npi>
//            <dest-addr-ton>1</dest-addr-ton>
//            <dest-addr-npi>1</dest-addr-npi>
//            <system-type>MCON1\,SINGLE</system-type>
//            <recconnect-timeout>30</recconnect-timeout>
//            <max-message>12</max-message>
//            <message-per-second>100</message-per-second>
//            <enquire-link-interval>10</enquire-link-interval>
//            <wait-resend-timeout>5</wait-resend-timeout>
//            <is-fake-channel>false</is-fake-channel>

//            Channel channel = new Channel();
//            channel.setName("smscru");
//            channel.setQueue("smscru");
//            channel.setSmppHost("localhost");
//            channel.setSmppPort(2775);
//            channel.setPayload(false);
//            channel.setSmppUserName("smppclient1");
//            channel.setSmppPassword("password");
//            channel.setSmppSourceAddr("0006543");
//            channel.setSmppSourceTon(5);
//            channel.setSmppDestNpi(1);
//            channel.setSmppDestTon(1);
//            channel.setSmppDestNpi(1);
//            channel.setSmppSystemType("MCON1\\,SINGLE");
//            channel.setSmppReconnectTimeOut(10);
//            channel.setSmppEnquireLinkInterval(20);
//            channel.setSmppSendMessagePerSecond(20);
//            channle.setSmppMaxMessage(12);
//            channel.setFake(false);
//            channel.setEnable(true);
//            channelDAO.create(channel);


//            channel = channelDAO.get(1);
//            Message message = new Message();
//            String test = "test";
//            message.setTo("79062783751");
//            message.setMessageData(test.getBytes());
//            message.setSequenceNumber(1);
//            message.setPayload(false);
//            messageDAO.create(message, channel);
//            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
//            session.beginTransaction();
//            Message message = new Message();
//            String test = "test";
//            message.setMessageData(test.getBytes());
//            message.setTo("79062783751");
//            message.setStatus("NEEDSEND");
//            message.setSequenceNumber(1);
//            session.save(message);
//            User user =  (User) session.get(User.class, 2);
//            for (Channel channel:user.getChannels()) {
//                System.out.println(channel.getName());
//            }
//            Channel channel = new Channel();
//            channel.setName("testchannel");
//            session.save(channel);
//            Channel channel1 = new Channel();
//            channel.setName("testchannel1");
//            session.save(channel);
//            User user = new User();
//            user.setLogin("test1");
//            user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
//            Set<Channel> channels = new HashSet<>();
//            channels.add(channel);
//            channels.add(channel1);
//            user.setChannels(channels);
//            session.save(user);
//            session.getTransaction().commit();
//            redisClient = new RedisClient(globalConfig);
//            SharedConfig.setRedisClient(redisClient);
//            RabbitMqOldMessageWatcher rabbitMqOldMessageWatcher = new RabbitMqOldMessageWatcher(globalConfig);
//            rabbitMqOldMessageWatcher.start();
//

            ChannelDAOImpl channelDAO = new ChannelDAOImpl();
            for (Object obj: channelDAO.getAllEnableList()) {
                AsyncSmsServer asyncSmsServer = new AsyncSmsServer(globalConfig, ((Channel) obj));
                asyncSmsServer.start();
            }

//            for (String channelName: globalConfig.getChannelList()) {
//                if (!globalConfig.getChennelConfig(channelName).isFakeChannel()) {
//                    AsyncSmsServer asyncSmsServer;
//                    asyncSmsServer = new AsyncSmsServer(globalConfig.getChennelConfig(channelName), redisClient);
//                    asyncSmsServer.start();
//                }
//            }

            server = new WebServer(globalConfig);
            try {
                server.start();
                server.join();
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }  catch (JedisDataException | JedisConnectionException e) {
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
