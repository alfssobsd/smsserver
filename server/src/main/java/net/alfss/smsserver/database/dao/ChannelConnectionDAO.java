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
    public List getList(int channelId);
    public List getList(Channel channel);
}
