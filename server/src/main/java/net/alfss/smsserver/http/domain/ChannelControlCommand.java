package net.alfss.smsserver.http.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * User: alfss
 * Date: 20.09.14
 * Time: 2:47
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChannelControlCommand {
    private Integer channelId;

    @JsonProperty("channel_id")
    public Integer getChannelId() {
        return channelId;
    }

    @JsonProperty("channel_id")
    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }
}
