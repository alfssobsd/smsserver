package net.alfss.smsserver.rabbit.prototype;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import net.alfss.smsserver.config.GlobalConfig;
import net.alfss.smsserver.rabbit.pool.RabbitMqPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * User: alfss
 * Date: 03.12.13
 * Time: 22:42
 */
public abstract class Queue {
    protected final Logger logger = (Logger) LoggerFactory.getLogger(String.valueOf(this.getClass()));
    protected final ObjectMapper mapper = new ObjectMapper();
    protected final RabbitMqPool pool;
    protected boolean needInit = true;
    protected boolean enableConsumer = false;
    protected GlobalConfig config;
    protected String exchangeName;
    protected String queueName;
    List<String> routingKeys = null;
    protected QueueingConsumer consumer;

    @SuppressWarnings("unchecked")
    public Queue(GlobalConfig config, boolean enableConsumer) {
        this.config = config;
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(1);
        this.pool = new RabbitMqPool(poolConfig, config);
        this.enableConsumer = enableConsumer;
    }

    public void setConsumer(QueueingConsumer consumer) {
        this.consumer = consumer;
    }

    public QueueingConsumer getConsumer() {
        return consumer;
    }

    public void setRoutingKeys(List<String> routingKeys) {
        this.routingKeys = routingKeys;
    }

    public List<String> getRoutingKeys() {
        return routingKeys;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setNeedInit(boolean needInit) {
        this.needInit = needInit;
    }

    public int getConnectTimeOutInMs() {
        return config.getRabbitConnectTimeOut() * 1000;
    }

    protected void createExchangeDirect(String exchangeName, Channel channel) throws IOException {
        // exchange, type, durable, autoDelete, arguments
        channel.exchangeDeclare(exchangeName, "direct", true, false, null);
    }

    protected QueueingConsumer createConsumer(String queueName, boolean autoAck, Channel channel) throws IOException {
        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, autoAck, consumer);
        return consumer;
    }

    protected void initConnect(Channel channel) throws IOException, InterruptedException {
        createQueue(channel);
        if (enableConsumer) setConsumer(createConsumer(getQueueName(), false, channel));
        setNeedInit(false);
    }

    protected void createQueue(Channel channel) throws IOException, InterruptedException { }

    protected void checkNeedInit(Channel channel) throws IOException, InterruptedException {
        if (isNeedInit()) {
            initConnect(channel);
        }
    }

    protected boolean isNeedInit() {
        return needInit;
    }

}
