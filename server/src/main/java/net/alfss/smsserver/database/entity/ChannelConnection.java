package net.alfss.smsserver.database.entity;

import javax.persistence.*;

/**
 * User: alfss
 * Date: 24.01.14
 * Time: 16:46
 */
@Entity
@Table(name="channel_connections")
public class ChannelConnection {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "channel_connection_entity_seq_gen")
    @SequenceGenerator(name = "channel_connection_entity_seq_gen", sequenceName = "channel_connection_seq")
    private int channeConnectionlId;

    @Column(name="smpp_system_type")
    private String smppSystemType;

    @ManyToOne
    public Channel channel;

    public int getChanneConnectionlId() {
        return channeConnectionlId;
    }

    public void setChanneConnectionlId(int channeConnectionlId) {
        this.channeConnectionlId = channeConnectionlId;
    }

    public String getSmppSystemType() {
        return smppSystemType;
    }

    public void setSmppSystemType(String smppSystemType) {
        this.smppSystemType = smppSystemType;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}

