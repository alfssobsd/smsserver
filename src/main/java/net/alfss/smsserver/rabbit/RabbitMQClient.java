package net.alfss.smsserver.rabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import net.alfss.smsserver.config.GlobalConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * User: alfss
 * Date: 14.10.13
 * Time: 12:08
 */
public class RabbitMQClient {

    private GlobalConfig globalConfig;
    final Logger logger = (Logger) LoggerFactory.getLogger(RabbitMQWatcher.class);

    public RabbitMQClient(GlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
    }

    public void sendMessage(String message) {
        try {
        ConnectionFactory factory =  new ConnectionFactory();
        factory.setHost(globalConfig.getRabbitHost());
        factory.setPort(globalConfig.getRabbitPort());
        factory.setVirtualHost(globalConfig.getRabbitVhost());
        factory.setUsername(globalConfig.getRabbitUser());
        factory.setPassword(globalConfig.getRabbitPassword());
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        String exchangeName = globalConfig.getRabbitQueue();

        channel.queueDeclare(exchangeName, false, false, false, null);
        channel.exchangeDeclare(exchangeName, "direct");
        channel.basicPublish("", exchangeName, null, message.getBytes());
        channel.close();
        connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
