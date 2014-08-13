package net.alfss.smsserver.rabbit.prototype;

import net.alfss.smsserver.config.GlobalConfig;
import net.alfss.smsserver.utils.ReflectionUtils;

import java.io.IOException;

/**
 * User: alfss
 * Date: 04.12.13
 * Time: 0:42
 */
public abstract class QueueDirectSimple extends QueueDirectMessage<String> {

    public QueueDirectSimple(GlobalConfig config, String exchangeName, String queueName, boolean enableConsumer) {
        super(config, exchangeName, queueName, enableConsumer);
    }

    @Override
    public Class getMessageClass() {
        return ReflectionUtils.getGenericParameterClass(this.getClass(), QueueDirectSimple.class, 0);
    }

    @Override
    protected String mapperObject(String stringMessage) throws IOException {
        return stringMessage;
    }
}
