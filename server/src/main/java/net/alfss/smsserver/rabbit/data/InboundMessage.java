package net.alfss.smsserver.rabbit.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * User: alfss
 * Date: 09.12.13
 * Time: 13:31
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class InboundMessage {
    private String from;
    private String to;
    private String messageText;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone="UTC")
    private Timestamp createTime;
    private Integer startPart;
    private Integer uniqueMessageNumber;
    private Integer expireTime;


    public InboundMessage() {
        this.createTime = new Timestamp(new Date().getTime());
        this.startPart = 0;
        this.expireTime = 0;
    }

    @JsonProperty("from")
    public String getFrom() {
        return from;
    }

    @JsonProperty("from")
    public void setFrom(String from) {
        this.from = from;
    }

    @JsonProperty("to")
    public String getTo() {
        return to;
    }

    @JsonProperty("to")
    public void setTo(String to) {
        this.to = to;
    }

    @JsonProperty("message_text")
    public String getMessageText() {
        return messageText;
    }

    @JsonProperty("message_text")
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    @JsonProperty("create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    @JsonProperty("create_time")
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @JsonProperty("start_part")
    public Integer getStartPart() {
        return startPart;
    }

    @JsonProperty("start_part")
    public void setStartPart(Integer startPart) {
        this.startPart = startPart;
    }

    @JsonProperty("unq_message_number")
    public Integer getUniqueMessageNumber() {
        return uniqueMessageNumber;
    }

    @JsonProperty("unq_message_number")
    public void setUniqueMessageNumber(Integer uniqueMessageNumber) {
        this.uniqueMessageNumber = uniqueMessageNumber;
    }

    @JsonProperty("expire_time")
    public Integer getExpireTime() {
        return expireTime;
    }

    @JsonProperty("expire_time")
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
