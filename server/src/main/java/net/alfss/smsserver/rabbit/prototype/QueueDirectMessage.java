package net.alfss.smsserver.rabbit.prototype;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.QueueingConsumer;
import net.alfss.smsserver.config.GlobalConfig;
import net.alfss.smsserver.rabbit.exceptions.RabbitMqQueueConnectException;
import net.alfss.smsserver.rabbit.exceptions.RabbitMqQueueMappingException;
import net.alfss.smsserver.utils.ReflectionUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * User: alfss
 * Date: 03.06.14
 * Time: 17:39
 */
public abstract class QueueDirectMessage<T> extends Queue {

    public QueueDirectMessage(GlobalConfig config, String exchangeName, String queueName, boolean enableConsumer) {
        super(config, enableConsumer);
        setQueueName(queueName);
        setExchangeName(exchangeName);
    }

    @Override
    protected void createQueue(Channel channel) throws IOException {
        Map<String, Object> arguments = new HashMap<>();
        //Need only for rabbitmq < 3.0
        //arguments.put("x-ha-policy", "all");
        try {
            channel.queueDeclare(getQueueName(), true, false, false, arguments);
            createExchangeDirect(getExchangeName(), channel);
            channel.queueBind(getQueueName(), getExchangeName(), getQueueName());
        } catch (Exception e) {
            throw new RabbitMqQueueConnectException(e);
        }
    }

    public void publish(T message) {
        Channel channel = null;
        try {
            channel = pool.getResource();
            checkNeedInit(channel);
            channel.basicPublish("", getExchangeName(), MessageProperties.PERSISTENT_BASIC, message.toString().getBytes());
            pool.returnResource(channel);
        } catch (Exception e) {
            setNeedInit(true);
            pool.returnBrokenResource(channel);
            throw new RabbitMqQueueConnectException(e);
        }
    }


    @SuppressWarnings("unchecked")
    public T getNextMessage() {
        Channel channel = null;
        try {
            channel = pool.getResource();
            checkNeedInit(channel);
            QueueingConsumer.Delivery delivery = getConsumer().nextDelivery();
            String stringMessage = new String(delivery.getBody());
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), true);
            T message = mapperObject(stringMessage);
            pool.returnResource(channel);
            return message;
        } catch (JsonMappingException | JsonParseException e ) {
            pool.returnResource(channel);
            throw new RabbitMqQueueMappingException(e);
        } catch (Exception e) {
            setNeedInit(true);
            pool.returnBrokenResource(channel);
            throw new RabbitMqQueueConnectException(e);
        }
    }

    public void purge() {
        Channel channel = null;
        try {
            channel = pool.getResource();
            channel.queuePurge(queueName);
            pool.returnResource(channel);
        } catch (Exception e) {
            setNeedInit(true);
            pool.returnBrokenResource(channel);
            throw new RabbitMqQueueConnectException(e);
        }
    }

    public Class getMessageClass() {
        return ReflectionUtils.getGenericParameterClass(this.getClass(), QueueDirectMessage.class, 0);
    }

    @SuppressWarnings("unchecked")
    protected T mapperObject(String stringMessage) throws IOException {
        return (T) mapper.readValue(stringMessage, getMessageClass());
    }
}
