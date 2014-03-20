package net.alfss.smsserver.http.domain;

import net.alfss.smsserver.rabbit.data.InboundMessage;

/**
 * User: alfss
 * Date: 17.10.13
 * Time: 13:39
 */
public class ChannelMessage {
    private InboundMessage message;

    public ChannelMessage(InboundMessage message) {
        this.message = message;
    }

    public InboundMessage getMessage() {
        return message;
    }

    public void setMessage(InboundMessage message) {
        this.message = message;
    }
}
