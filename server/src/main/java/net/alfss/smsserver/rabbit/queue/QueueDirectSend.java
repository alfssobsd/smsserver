package net.alfss.smsserver.rabbit.queue;

import net.alfss.smsserver.config.GlobalConfig;
import net.alfss.smsserver.database.entity.Channel;
import net.alfss.smsserver.rabbit.prototype.QueueDirectSimple;

/**
 * User: alfss
 * Date: 09.12.13
 * Time: 13:43
 */
public class QueueDirectSend extends QueueDirectSimple {
    public QueueDirectSend(GlobalConfig config, String exchangeName, String queueName, boolean enableConsumer) {
        super(config, exchangeName, queueName, enableConsumer);
    }

    public QueueDirectSend(GlobalConfig config, Channel channel, boolean enableConsumer) {
        this(config,
             config.getRabbitQueuePrefix() + channel.getQueueName() + "-send",
             config.getRabbitQueuePrefix() + channel.getQueueName() + "-send",
             enableConsumer);
    }
}
