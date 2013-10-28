package net.alfss.smsserver.http.domain;

/**
 * User: alfss
 * Date: 17.10.13
 * Time: 13:27
 */
public class Channel {
    private String channelName;
    private String channelQueue;
    private Long channelQueueSize;

    public Channel(String channelName, String channelQueue, Long channelQueueSize) {
        this.channelName = channelName;
        this.channelQueue = channelQueue;
        this.channelQueueSize = channelQueueSize;
    }

    public Channel() {
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelQueue() {
        return channelQueue;
    }

    public void setChannelQueue(String channelQueue) {
        this.channelQueue = channelQueue;
    }

    public Long getChannelQueueSize() {
        return channelQueueSize;
    }

    public void setChannelQueueSize(Long channelQueueSize) {
        this.channelQueueSize = channelQueueSize;
    }
}
