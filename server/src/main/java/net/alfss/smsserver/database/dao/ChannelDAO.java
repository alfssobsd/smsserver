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
    public void update(Channel channel);
    public List getList(int limit, int offset);
    public List getEnableList(int limit, int offset);
    public List getAllEnableList();
}
