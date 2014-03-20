package net.alfss.smsserver.database.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * User: alfss
 * Date: 05.12.13
 * Time: 17:02
 */
@Entity
@Table(name="channels")
public class Channel {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "channel_entity_seq_gen")
    @SequenceGenerator(name = "channel_entity_seq_gen", sequenceName = "channels_id_seq")
    private int channelId;

    @Column(name="name", unique = true, nullable = false)
    private String name;

    @Column(name="queue_name", nullable = false)
    private String queueName;

    @Column(name="smpp_host", nullable = false)
    private String smppHost;

    @Column(name="smpp_port", nullable = false)
    private int smppPort;

    @Column(name="smpp_username", nullable = false)
    private String smppUserName;

    @Column(name="smpp_password", nullable = false)
    private String smppPassword;

    @Column(name="smpp_source_addr")
    private String smppSourceAddr;

    @Column(name="smpp_source_ton", nullable = false)
    private int smppSourceTon;

    @Column(name="smpp_source_npi", nullable = false)
    private int smppSourceNpi;

    @Column(name="smpp_dest_ton", nullable = false)
    private int smppDestTon;

    @Column(name="smpp_dest_npi", nullable = false)
    private int smppDestNpi;

    @Column(name="smpp_max_split_message")
    private int smppMaxSplitMessage;

    @Column(name="smpp_max_message_per_second", nullable = false)
    private int smppMaxMessagePerSecond;

    @Column(name="smpp_reconnect_timeout")
    private int smppReconnectTimeOut;

    @Column(name="smpp_enquire_link_interval")
    private int smppEnquireLinkInterval;

    @Column(name="is_payload")
    private boolean payload = false;

    @Column(name="is_fake")
    private Boolean fake;

    @Column(name="is_enable")
    private Boolean enable = true;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "channel", cascade = CascadeType.ALL)
    public List<Message> messages;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  mappedBy = "channel")
    private Set<ChannelConnection> channelConnections;

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getSmppHost() {
        return smppHost;
    }

    public void setSmppHost(String smppHost) {
        this.smppHost = smppHost;
    }

    public int getSmppPort() {
        return smppPort;
    }

    public void setSmppPort(int smppPort) {
        this.smppPort = smppPort;
    }

    public String getSmppUserName() {
        return smppUserName;
    }

    public void setSmppUserName(String smppUserName) {
        this.smppUserName = smppUserName;
    }

    public String getSmppPassword() {
        return smppPassword;
    }

    public void setSmppPassword(String smppPassword) {
        this.smppPassword = smppPassword;
    }

    public String getSmppSourceAddr() {
        return smppSourceAddr;
    }

    public void setSmppSourceAddr(String smppSourceAddr) {
        this.smppSourceAddr = smppSourceAddr;
    }

    public int getSmppSourceTon() {
        return smppSourceTon;
    }

    public void setSmppSourceTon(int smppSourceTon) {
        this.smppSourceTon = smppSourceTon;
    }

    public int getSmppSourceNpi() {
        return smppSourceNpi;
    }

    public void setSmppSourceNpi(int smppSourceNpi) {
        this.smppSourceNpi = smppSourceNpi;
    }

    public int getSmppDestTon() {
        return smppDestTon;
    }

    public void setSmppDestTon(int smppDestTon) {
        this.smppDestTon = smppDestTon;
    }

    public int getSmppDestNpi() {
        return smppDestNpi;
    }

    public void setSmppDestNpi(int smppDestNpi) {
        this.smppDestNpi = smppDestNpi;
    }

    public int getSmppMaxMessagePerSecond() {
        return smppMaxMessagePerSecond;
    }

    public void setSmppMaxMessagePerSecond(int smppMaxMessagePerSecond) {
        this.smppMaxMessagePerSecond = smppMaxMessagePerSecond;
    }

    public int getSmppReconnectTimeOut() {
        return smppReconnectTimeOut;
    }

    public void setSmppReconnectTimeOut(int smppReconnectTimeOut) {
        this.smppReconnectTimeOut = smppReconnectTimeOut;
    }

    public int getSmppEnquireLinkInterval() {
        return smppEnquireLinkInterval;
    }

    public void setSmppEnquireLinkInterval(int smppEnquireLinkInterval) {
        this.smppEnquireLinkInterval = smppEnquireLinkInterval;
    }

    public Boolean getFake() {
        return fake;
    }

    public void setFake(Boolean fake) {
        this.fake = fake;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public boolean isPayload() {
        return payload;
    }

    public void setPayload(boolean payload) {
        this.payload = payload;
    }

    public int getSmppMaxSplitMessage() {
        return smppMaxSplitMessage;
    }

    public void setSmppMaxSplitMessage(int smppMaxSplitMessage) {
        this.smppMaxSplitMessage = smppMaxSplitMessage;
    }

    public Set<ChannelConnection> getChannelConnections() {
        return channelConnections;
    }

    public void setChannelConnections(Set<ChannelConnection> channelConnections) {
        this.channelConnections = channelConnections;
    }
}
