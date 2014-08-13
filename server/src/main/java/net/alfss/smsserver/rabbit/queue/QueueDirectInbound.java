package net.alfss.smsserver.rabbit.queue;

import net.alfss.smsserver.config.GlobalConfig;
import net.alfss.smsserver.database.entity.Channel;
import net.alfss.smsserver.rabbit.data.InboundMessage;
import net.alfss.smsserver.rabbit.prototype.QueueDirectMessage;

/**
 * User: alfss
 * Date: 09.12.13
 * Time: 13:42
 */
public class QueueDirectInbound extends QueueDirectMessage<InboundMessage> {
    public QueueDirectInbound(GlobalConfig config, String exchangeName, String queueName, boolean enableConsumer) {
        super(config, exchangeName, queueName, enableConsumer);
    }

    public QueueDirectInbound(GlobalConfig config, Channel channel, boolean enableConsumer) {
        this(config,
             config.getRabbitQueuePrefix() + channel.getQueueName(),
             config.getRabbitQueuePrefix() + channel.getQueueName(),
             enableConsumer);
    }
}
