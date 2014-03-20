package net.alfss.smsserver.http.services;

import net.alfss.smsserver.database.dao.impl.ChannelDAOImpl;
import net.alfss.smsserver.database.entity.Channel;
import net.alfss.smsserver.http.dao.ConfigDao;
import net.alfss.smsserver.http.dao.RedisClientDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: alfss
 * Date: 17.10.13
 * Time: 14:32
 */
@Service
public class ChannelServices {

    private RedisClientDao redisClientDao;
    private ConfigDao configDao;
    final private ChannelDAOImpl channelDAO;

    public ChannelServices() {
        this.channelDAO = new ChannelDAOImpl();
    }

    @Autowired
    public void setChannelDao(RedisClientDao redisClientDao, ConfigDao configDao) {
        this.redisClientDao = redisClientDao;
        this.configDao = configDao;
    }

    public List getChannelList() {
        return channelDAO.getAllEnableList();
    }

    public Channel getChannelById(int channelId) {
        return channelDAO.get(channelId);
    }

}
