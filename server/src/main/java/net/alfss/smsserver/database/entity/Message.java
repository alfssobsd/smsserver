package net.alfss.smsserver.database.entity;

import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * User: alfss
 * Date: 06.12.13
 * Time: 3:22
 */
@Entity
@Table(name="messages")
public class Message {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "message_entity_seq_gen")
    @SequenceGenerator(name = "message_entity_seq_gen", sequenceName = "message_seq")
    private int messageId;

    @Column(name = "message_id")
    private String messageSmsId;

    @Column(name = "send_from")
    private String from;

    @Column(name = "send_to", nullable = false)
    private String to;

    @Column(name = "is_payload")
    private boolean payload = false;

    @Column(name = "sequence_number")
    private int sequenceNumber;

    @Column(name = "esm_class")
    private byte esmClass;

    @Column(name = "message_data", nullable = false)
    private byte [] messageData;

    @Index(name = "create_datetime_idx")
    @Column(name = "create_datetime", nullable = false )
    private Timestamp createTime = new Timestamp(new Date().getTime());

    @Index(name = "update_datetime_idx")
    @Column(name = "update_datetime", nullable = false)
    private Timestamp updateTime = new Timestamp(new Date().getTime());

    @Column(name = "queue_name")
    private String queueName;

    @Column(name = "send_retry")
    private int sendRetry = 0;

    @ManyToOne
    public Channel channel;

    @ManyToOne
    public Status status;


    public String getMessageSmsId() {
        return messageSmsId;
    }

    public void setMessageSmsId(String messageSmsId) {
        this.messageSmsId = messageSmsId;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public boolean isPayload() {
        return payload;
    }

    public void setPayload(boolean payload) {
        this.payload = payload;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public byte getEsmClass() {
        return esmClass;
    }

    public void setEsmClass(byte esmClass) {
        this.esmClass = esmClass;
    }

    public byte[] getMessageData() {
        return messageData;
    }

    public void setMessageData(byte[] messageData) {
        this.messageData = messageData;
    }

    public String getMessageDataString() {
        try {
            return new String(getMessageData(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "!@!# error encode";
        }
    }


    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public int getSendRetry() {
        return sendRetry;
    }

    public void setSendRetry(int sendRetry) {
        this.sendRetry = sendRetry;
    }
}
