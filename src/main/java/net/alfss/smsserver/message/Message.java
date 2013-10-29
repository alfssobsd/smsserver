package net.alfss.smsserver.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * User: alfss
 * Date: 01.10.13
 * Time: 16:59
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {
    private String channel;
    private String sourceAddress;
    private String destinationAddress;
    private String messageText;
    private Integer priority;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone="UTC")
    private Timestamp createTime;
    private Integer startPart;
    private Integer messageId;
    private Integer expireTime;

    public Message() {
        Date now = new Date();
        this.createTime = new Timestamp(now.getTime());
        this.startPart = 0;
        this.expireTime = 0;
    }


    @JsonProperty("from")
    public String getSourceAddress() {
        return sourceAddress;
    }

    @JsonProperty("from")
    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    @JsonProperty("to")
    public String getDestinationAddress() {
        return destinationAddress;
    }

    @JsonProperty("to")
    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    @JsonProperty("messagetext")
    public String getMessageText() {
        return messageText;
    }

    @JsonProperty("messagetext")
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }


    @JsonProperty("priority")
    public Integer getPriority() {
        return priority;
    }

    @JsonProperty("priority")
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @JsonProperty("channel")
    public String getChannel() {
        return channel;
    }

    @JsonProperty("channel")
    public void setChannel(String channel) {
        this.channel = channel;
    }

    @JsonProperty("createtime")
    public Timestamp getCreateTime() {
        return createTime;
    }

    @JsonProperty("createtime")
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @JsonProperty("startpart")
    public Integer getStartPart() {
        return startPart;
    }

    @JsonProperty("startpart")
    public void setStartPart(Integer startPart) {
        this.startPart = startPart;
    }

    @JsonProperty("messageid")
    public Integer getMessageId() {
        return messageId;
    }

    @JsonProperty("messageid")
    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    @JsonProperty("expiretime")
    public Integer getExpireTime() {
        return expireTime;
    }

    @JsonProperty("expiretime")
    public void setExpireTime(Integer expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (IOException e) {
            return null;
        }
    }

    public boolean isExpired()  {
        if (!expireTime.equals(0) && expireTime > 0) {
            Date now = new Date();
            return now.getTime() > (createTime.getTime() + (long) (this.expireTime * 1000));
        }
        return false;
    }
}
