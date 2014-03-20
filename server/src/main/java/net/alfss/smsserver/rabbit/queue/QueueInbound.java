package net.alfss.smsserver.rabbit.queue;

import net.alfss.smsserver.config.GlobalConfig;
import net.alfss.smsserver.rabbit.data.InboundMessage;
import net.alfss.smsserver.rabbit.prototype.QueueRoutingMessage;

import java.util.List;

/**
 * User: alfss
 * Date: 09.12.13
 * Time: 13:42
 */
public class QueueInbound extends QueueRoutingMessage<InboundMessage> {
    public QueueInbound(GlobalConfig config, String exchangeName, String queueName, boolean enableConsumer, List<String> routingKeys) {
        super(config, exchangeName, queueName, enableConsumer, routingKeys);
    }
}
