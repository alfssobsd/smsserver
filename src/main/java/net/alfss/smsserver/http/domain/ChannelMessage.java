package net.alfss.smsserver.http.domain;

import net.alfss.smsserver.message.Message;

/**
 * User: alfss
 * Date: 17.10.13
 * Time: 13:39
 */
public class ChannelMessage {
    private Message message;

    public ChannelMessage(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
