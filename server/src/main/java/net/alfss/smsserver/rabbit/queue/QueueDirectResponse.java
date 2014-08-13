package net.alfss.smsserver.rabbit.queue;

import net.alfss.smsserver.config.GlobalConfig;
import net.alfss.smsserver.database.entity.Channel;
import net.alfss.smsserver.rabbit.data.ResponseMessage;
import net.alfss.smsserver.rabbit.prototype.QueueDirectMessage;

/**
 * User: alfss
 * Date: 27.06.14
 * Time: 20:54
 */
public class QueueDirectResponse extends QueueDirectMessage<ResponseMessage> {

    public QueueDirectResponse(GlobalConfig config, String exchangeName, String queueName, boolean enableConsumer) {
        super(config, exchangeName, queueName, enableConsumer);
    }

    public QueueDirectResponse(GlobalConfig config, Channel channel, boolean enableConsumer) {
        this(config,
             config.getRabbitQueuePrefix() + channel.getQueueName() + "-response",
             config.getRabbitQueuePrefix() + channel.getQueueName() + "-response",
             enableConsumer);
    }
}
