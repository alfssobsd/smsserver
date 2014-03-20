package net.alfss.smsserver.rabbit.prototype;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.QueueingConsumer;
import net.alfss.smsserver.config.GlobalConfig;
import net.alfss.smsserver.rabbit.exceptions.RabbitMqQueueConnectException;
import net.alfss.smsserver.utils.ReflectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * User: alfss
 * Date: 04.12.13
 * Time: 0:42
 */
public abstract class QueueSimple extends Queue {

    public QueueSimple(GlobalConfig config, String exchangeName, String queueName, boolean enableConsumer) {
        super(config, enableConsumer);
        setQueueName(queueName);
        setExchangeName(exchangeName);
    }

    @Override
    protected void createQueue(Channel channel)  {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-ha-policy", "all");
        try {
            channel.queueDeclare(getQueueName(), true, false, false, arguments);
            createExchangeDirect(getExchangeName(), channel);
            channel.queueBind(getQueueName(), getExchangeName(), getQueueName());
        } catch (Exception e) {
            throw new RabbitMqQueueConnectException(e);
        }
    }

    public void publish(String message) {
        Channel channel = null;
        try {
            channel = pool.getResource();
            checkNeedInit(channel);
            channel.basicPublish("", getExchangeName(), MessageProperties.PERSISTENT_BASIC, message.getBytes());
            pool.returnResource(channel);
        } catch (Exception e) {
            setNeedInit(true);
            pool.returnBrokenResource(channel);
            throw new RabbitMqQueueConnectException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public String getNextMessage() {
        Channel channel = null;
        try {
            channel = pool.getResource();
            checkNeedInit(channel);
            QueueingConsumer.Delivery delivery = getConsumer().nextDelivery();
            String message = new String(delivery.getBody());
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), true);
            pool.returnResource(channel);
            return message;
        } catch (Exception e) {
            setNeedInit(true);
            pool.returnBrokenResource(channel);
            throw new RabbitMqQueueConnectException(e);
        }
    }

    public Class getMessageClass() {
        return ReflectionUtils.getGenericParameterClass(this.getClass(), QueueSimple.class, 0);
    }
}
