package net.alfss.smsserver.database.dao;

import net.alfss.smsserver.database.entity.Channel;

import java.util.List;

/**
 * User: alfss
 * Date: 09.12.13
 * Time: 16:47
 */
public interface ChannelDAO {
    public Channel get(int channelId);
    public void create(Channel channel);
    public List getAllEnableList();
}
