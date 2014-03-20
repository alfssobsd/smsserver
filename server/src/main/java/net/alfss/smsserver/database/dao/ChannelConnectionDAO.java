package net.alfss.smsserver.database.dao;

import net.alfss.smsserver.database.entity.Channel;
import net.alfss.smsserver.database.entity.ChannelConnection;

import java.util.List;

/**
 * User: alfss
 * Date: 24.01.14
 * Time: 16:46
 */
public interface  ChannelConnectionDAO {
    public ChannelConnection get(int channelConnectionId);
    public void create(int channelId, String smppSystemType);
    public void create(Channel channel, String smppSystemType);
    public void update(ChannelConnection channelConnection);
    public void delete(ChannelConnection channelConnection);
    public List getList(int channelId);
    public List getList(Channel channel);
}
