package net.alfss.smsserver.http.services;

import net.alfss.smsserver.config.ChannelConfig;
import net.alfss.smsserver.http.dao.ConfigDao;
import net.alfss.smsserver.http.dao.RedisClientDao;
import net.alfss.smsserver.http.domain.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Autowired
    public void setChannelDao(RedisClientDao redisClientDao, ConfigDao configDao) {
        this.redisClientDao = redisClientDao;
        this.configDao = configDao;
    }

    public List<Channel> getChannelList() {
        ArrayList<Channel> channelList = new ArrayList<Channel>();
        for(String channelName:configDao.getAllChannelName()) {
            channelList.add(getChannel(channelName));
        }
        return channelList;
    }

    public Channel getChannel(String channelName) {
        ChannelConfig channelConfig = configDao.getChannelConfig(channelName);
        Channel channel = new Channel();
        channel.setChannelName(channelConfig.getChannel());
        channel.setChannelQueue(channelConfig.getChannelQueue());
        channel.setChannelQueueSize(redisClientDao.countMessageInChannel(channel.getChannelQueue()));

        return channel;
    }

}
