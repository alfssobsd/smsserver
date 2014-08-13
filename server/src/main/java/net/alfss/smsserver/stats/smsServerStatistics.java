package net.alfss.smsserver.stats;

/**
 * User: alfss
 * Date: 28.07.14
 * Time: 17:42
 */
public class SmsServerStatistics {
    private long sendMessagePerMinute;
    private long errorSendMessagePerMinute;
    private long errorDeliveryMessagePerMinute;
    private long secondsAfterLastReconnect;
    private boolean serverIsRunning;
    private boolean serverIsConnectedToSmpp;
    private boolean serverIsConnectedToRabbitMq;


    public synchronized long getSendMessagePerMinute() {
        return sendMessagePerMinute;
    }
}
