package net.alfss.smsserver.rabbit.queue;

import net.alfss.smsserver.config.GlobalConfig;
import net.alfss.smsserver.rabbit.prototype.QueueSimple;

/**
 * User: alfss
 * Date: 09.12.13
 * Time: 13:43
 */
public class QueueSend extends QueueSimple {
    public QueueSend(GlobalConfig config, String exchangeName, String queueName, boolean enableConsumer) {
        super(config, exchangeName, queueName, enableConsumer);
    }
}
