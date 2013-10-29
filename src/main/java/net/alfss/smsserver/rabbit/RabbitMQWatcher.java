package net.alfss.smsserver.rabbit;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import net.alfss.smsserver.config.GlobalConfig;
import net.alfss.smsserver.message.Message;
import net.alfss.smsserver.redis.RedisClient;
import net.alfss.smsserver.redis.exceptions.RedisUnknownError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.io.IOException;

/**
 * User: alfss
 * Date: 01.10.13
 * Time: 16:09
 */
public class RabbitMQWatcher extends Thread {
    protected final int sleepTime = 10;
    protected final int connectTimeOut = 10;
    private GlobalConfig globalConfig;
    private RedisClient redisClient;
    private QueueingConsumer consumer;
    final Logger logger = (Logger) LoggerFactory.getLogger(RabbitMQWatcher.class);

    public RabbitMQWatcher(GlobalConfig globalConfig, RedisClient redisClient) {
        this.globalConfig = globalConfig;
        this.redisClient = redisClient;
    }

    @Override
    public void run() {
        logger.warn("RabbitMQWatcher: Start");
        this.subscribeRabbit();
        logger.warn("RabbitMQWatcher: Stop");
    }

    public boolean reconnectRabbit() {
        logger.error("RabbitMQWatcher: Reconnect to RabbitMQ server after " + sleepTime + " sec");
        try {
            sleep(1000 * sleepTime);
            return this.connectRabbit();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        return false;
    }

    public boolean connectRabbit() {
        ConnectionFactory factory =  new ConnectionFactory();
        factory.setHost(globalConfig.getRabbitHost());
        factory.setPort(globalConfig.getRabbitPort());
        factory.setVirtualHost(globalConfig.getRabbitVhost());
        factory.setUsername(globalConfig.getRabbitUser());
        factory.setPassword(globalConfig.getRabbitPassword());
        factory.setConnectionTimeout(1000 * connectTimeOut);

        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            logger.warn("RabbitMQWatcher: Connected to RabbitMQ server");

            String exchangeName = globalConfig.getRabbitQueue();

            channel.queueDeclare(exchangeName, false, false, false, null);
            channel.exchangeDeclare(exchangeName, "direct");
            channel.queueBind(exchangeName, exchangeName, "");
            consumer = new QueueingConsumer(channel);
            channel.basicConsume(exchangeName, true, consumer);

            logger.warn("RabbitMQWatcher: Subscribe RabbitMQ queue: vhost:" + globalConfig.getRabbitVhost() + " -> queue:" + globalConfig.getRabbitQueue());

            return true;
        } catch (IOException e) {
            logger.error("RabbitMQWatcher: Error connect to RabbitMQ server");
            logger.error(e.toString());
            for(StackTraceElement traceElement:e.getStackTrace()){
                logger.debug(traceElement.toString());
            }
        }

        return false;
    }

    public void subscribeRabbit() {
        boolean flag = true;
        boolean needPushMessage = false;
        boolean notConnected = !this.connectRabbit();
        while(notConnected) {
            notConnected = !this.reconnectRabbit();
        }
        while (flag) {
            try {
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                String message = new String(delivery.getBody());
                ObjectMapper mapper = new ObjectMapper();
                do {
                    try {
                        redisClient.pushMessageToList(mapper.readValue(message, Message.class));
                        needPushMessage = false;
                    } catch (JedisConnectionException | RedisUnknownError e) {
                        logger.error("RabbitMQWatcher: error connect to redis " + e.toString());
                        sleep(redisClient.getConnectTimeOutInMs());
                        needPushMessage = true;
                    } catch (NullPointerException e) {
                        needPushMessage = false;
                        logger.error("RabbitMQWatcher: error push redis message = " + message + ", " + e.toString());
                    }
                } while (needPushMessage);
            } catch (InterruptedException e) {
                logger.error(e.toString());
                for(StackTraceElement traceElement:e.getStackTrace()){
                    logger.debug(traceElement.toString());
                }
                this.reconnectRabbit();
            } catch (ShutdownSignalException e) {
                logger.error(e.toString());
                this.reconnectRabbit();
            } catch (JsonMappingException e) {
                logger.error("RabbitMQWatcher: Json(error Mapping) " + e.toString());
            } catch (JsonParseException e) {
                logger.error("RabbitMQWatcher: Json(error Parse) " + e.toString());
            } catch (IOException e) {
                logger.error("RabbitMQWatcher: " + e.toString());
            }
        }

    }
}
