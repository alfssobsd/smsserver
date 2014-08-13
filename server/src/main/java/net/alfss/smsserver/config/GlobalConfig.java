package net.alfss.smsserver.config;

import net.alfss.smsserver.utils.HibernateUtil;
import org.apache.commons.configuration.XMLConfiguration;

/**
 * User: alfss
 * Date: 01.10.13
 * Time: 16:22
 */
public class GlobalConfig {
    //database
    private String dbUrl;
    private String dbUserName;
    private String dbPassword;
    private int dbPoolSize;

    //rabbit
    private String rabbitHost;
    private int    rabbitPort;
    private String rabbitVhost;
    private String rabbitQueueMessageOld;
    private String rabbitQueuePrefix;
    private String rabbitQueueInternalMessage;
    private int rabbitConnectTimeOut;
    private String rabbitUser;
    private String rabbitPassword;
    //redis
    private int redisMaxPool;
    private String redisHost;
    private int redisPort;
    private int redisTimeOut;
    private String redisPassword;
    private int redisDatabase;
    //jetty
    private int jettyPort;
    private String jettyAddress;
    private int jettyMinPool;
    private int jettyMaxPool;
    private int jettyOutputBufferSize;
    private int jettyRequestHeaderSize;
    private int jettyResponseHeaderSize;

    public GlobalConfig(XMLConfiguration xml_config) {
        //db
        setDbUrl(xml_config.getString("db-url", "jdbc:postgresql://localhost:5432/smsserver"));
        setDbUserName(xml_config.getString("db-username", "postgres"));
        setDbPassword(xml_config.getString("db-password", ""));
        setDbPoolSize(xml_config.getInt("db-pool-size", 1));
        //db setup
        HibernateUtil.setupConnection(this);

        //rabbit
        setRabbitHost(xml_config.getString("rabbit-host", "localhost"));
        setRabbitPort(xml_config.getInt("rabbit-port", 5672));
        setRabbitVhost(xml_config.getString("rabbit-vhost", "/smsserver"));
        setRabbitQueueMessageOld(xml_config.getString("rabbit-queue-message-old", "message-old"));
        setRabbitQueuePrefix(xml_config.getString("rabbit-queue-prefix", "smsserver-message-"));
        setRabbitQueueInternalMessage(xml_config.getString("rabbit-queue-internal-message", "smsserver-raw-message"));
        setRabbitConnectTimeOut(xml_config.getInt("rabbit-connect-timeout", 10));
        setRabbitUser(xml_config.getString("rabbit-user", "guest"));
        setRabbitPassword(xml_config.getString("rabbit-password", "guest"));
        //redis
        setRedisPassword(xml_config.getString("redis-password", null));
        setRedisMaxPool(xml_config.getInt("redis-max-pool", 12));
        setRedisTimeOut(xml_config.getInt("redis-timeout", 10));
        setRedisHost(xml_config.getString("redis-host", "localhost"));
        setRedisPort(xml_config.getInt("redis-port", 6379));
        setRedisDatabase(xml_config.getInt("redis-db", 0));
        //jetty
        setJettyAddress(xml_config.getString("http-address", "0.0.0.0"));
        setJettyPort(xml_config.getInt("http-port", 8080));
        setJettyMinPool(xml_config.getInt("http-min-pool", 1));
        setJettyMaxPool(xml_config.getInt("http-max-pool", 10));
        setJettyOutputBufferSize(xml_config.getInt("http-out-buffer", 32768));
        setJettyRequestHeaderSize(xml_config.getInt("http-req-header", 8192));
        setJettyResponseHeaderSize(xml_config.getInt("http-res-header", 8192));

    }

    public String getRabbitHost() {
        return rabbitHost;
    }

    public void setRabbitHost(String rabbitHost) {
        this.rabbitHost = rabbitHost;
    }

    public int getRabbitPort() {
        return rabbitPort;
    }

    public void setRabbitPort(int rabbitPort) {
        this.rabbitPort = rabbitPort;
    }

    public String getRabbitVhost() {
        return rabbitVhost;
    }

    public void setRabbitVhost(String rabbitVhost) {
        this.rabbitVhost = rabbitVhost;
    }

    public String getRabbitQueueMessageOld() {
        return rabbitQueueMessageOld;
    }

    public void setRabbitQueueMessageOld(String rabbitQueueMessageOld) {
        this.rabbitQueueMessageOld = rabbitQueueMessageOld;
    }

    public String getRabbitUser() {
        return rabbitUser;
    }

    public void setRabbitUser(String rabbitUser) {
        this.rabbitUser = rabbitUser;
    }

    public String getRabbitPassword() {
        return rabbitPassword;
    }

    public void setRabbitPassword(String rabbitPassword) {
        this.rabbitPassword = rabbitPassword;
    }

    public int getRedisMaxPool() {
        return redisMaxPool;
    }

    public void setRedisMaxPool(int redisMaxPool) {
        this.redisMaxPool = redisMaxPool;
    }

    public String getRedisHost() {
        return redisHost;
    }

    public void setRedisHost(String redisHost) {
        this.redisHost = redisHost;
    }

    public int getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(int redisPort) {
        this.redisPort = redisPort;
    }

    public int getRedisTimeOut() {
        return redisTimeOut;
    }

    public void setRedisTimeOut(int redisTimeOut) {
        this.redisTimeOut = redisTimeOut;
    }

    public String getRedisPassword() {
        return redisPassword;
    }

    public void setRedisPassword(String redisPassword) {
        this.redisPassword = redisPassword;
    }

    public int getRedisDatabase() {
        return redisDatabase;
    }

    public void setRedisDatabase(int redisDatabase) {
        this.redisDatabase = redisDatabase;
    }

    public int getJettyPort() {
        return jettyPort;
    }

    public void setJettyPort(int jettyPort) {
        this.jettyPort = jettyPort;
    }

    public String getJettyAddress() {
        return jettyAddress;
    }

    public void setJettyAddress(String jettyAddress) {
        this.jettyAddress = jettyAddress;
    }

    public int getJettyMinPool() {
        return jettyMinPool;
    }

    public void setJettyMinPool(int jettyMinPool) {
        this.jettyMinPool = jettyMinPool;
    }

    public int getJettyMaxPool() {
        return jettyMaxPool;
    }

    public void setJettyMaxPool(int jettyMaxPool) {
        this.jettyMaxPool = jettyMaxPool;
    }

    public int getJettyOutputBufferSize() {
        return jettyOutputBufferSize;
    }

    public void setJettyOutputBufferSize(int jettyOutputBufferSize) {
        this.jettyOutputBufferSize = jettyOutputBufferSize;
    }

    public int getJettyRequestHeaderSize() {
        return jettyRequestHeaderSize;
    }

    public void setJettyRequestHeaderSize(int jettyRequestHeaderSize) {
        this.jettyRequestHeaderSize = jettyRequestHeaderSize;
    }

    public int getJettyResponseHeaderSize() {
        return jettyResponseHeaderSize;
    }

    public void setJettyResponseHeaderSize(int jettyResponseHeaderSize) {
        this.jettyResponseHeaderSize = jettyResponseHeaderSize;
    }

    public String getRabbitQueueInternalMessage() {
        return rabbitQueueInternalMessage;
    }

    public void setRabbitQueueInternalMessage(String rabbitQueueInternalMessage) {
        this.rabbitQueueInternalMessage = rabbitQueueInternalMessage;
    }

    public String getRabbitQueuePrefix() {
        return rabbitQueuePrefix;
    }

    public void setRabbitQueuePrefix(String rabbitQueuePrefix) {
        this.rabbitQueuePrefix = rabbitQueuePrefix;
    }

    public int getRabbitConnectTimeOut() {
        return rabbitConnectTimeOut;
    }

    public void setRabbitConnectTimeOut(int rabbitConnectTimeOut) {
        this.rabbitConnectTimeOut = rabbitConnectTimeOut;
    }

    public int getDbPoolSize() {
        return dbPoolSize;
    }

    public void setDbPoolSize(int dbPoolSize) {
        this.dbPoolSize = dbPoolSize;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getDbUserName() {
        return dbUserName;
    }

    public void setDbUserName(String dbUserName) {
        this.dbUserName = dbUserName;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }
}
