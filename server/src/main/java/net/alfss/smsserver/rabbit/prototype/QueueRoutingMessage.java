package net.alfss.smsserver.rabbit.prototype;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import net.alfss.smsserver.config.GlobalConfig;
import net.alfss.smsserver.rabbit.exceptions.RabbitMqQueueConnectException;
import net.alfss.smsserver.utils.ReflectionUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: alfss
 * Date: 04.12.13
 * Time: 0:07
 */
public abstract class QueueRoutingMessage<T> extends QueueDirectMessage<T> {

    public QueueRoutingMessage(GlobalConfig config, String exchangeName, String queueName, boolean enableConsumer, List<String> routingKeys) {
        super(config, exchangeName, queueName, enableConsumer);
        setQueueName(queueName);
        setRoutingKeys(routingKeys);
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
            if (getRoutingKeys() != null) {
                for (String key : getRoutingKeys() ) {
                    channel.queueBind(getQueueName(), getExchangeName(), key);
                }
            }
        } catch (Exception e) {
            throw new RabbitMqQueueConnectException(e);
        }
    }

    public void publish(String routeKey, T message) {
        Channel channel = null;
        try {
            channel = pool.getResource();
            checkNeedInit(channel);
            channel.basicPublish(getExchangeName(), routeKey, MessageProperties.PERSISTENT_BASIC, message.toString().getBytes());
            pool.returnResource(channel);
        } catch (Exception e) {
            setNeedInit(true);
            pool.returnBrokenResource(channel);
            throw new RabbitMqQueueConnectException(e);
        }
    }

    @Override
    public Class getMessageClass() {
        return ReflectionUtils.getGenericParameterClass(this.getClass(), QueueRoutingMessage.class, 0);
    }


    @Override
    @SuppressWarnings("unchecked")
    protected T mapperObject(String stringMessage) throws IOException {
        return (T) mapper.readValue(stringMessage, getMessageClass());
    }
}
