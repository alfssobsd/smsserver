package net.alfss.smsserver.http.services;

import net.alfss.smsserver.config.UserConfig;
import net.alfss.smsserver.http.dao.ConfigDao;
import net.alfss.smsserver.http.dao.RedisClientDao;
import net.alfss.smsserver.http.domain.ChannelUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * User: alfss
 * Date: 18.10.13
 * Time: 1:39
 */
@Service
public class UserServices {

    private RedisClientDao redisClientDao;
    private ConfigDao configDao;

    @Autowired
    private ChannelServices channelServices;

    @Autowired
    public void setDao(RedisClientDao redisClientDao, ConfigDao configDao) {
        this.redisClientDao = redisClientDao;
        this.configDao = configDao;
    }

    public List<ChannelUser> getChannelUserList() {
        ArrayList<ChannelUser> channelUserList = new ArrayList<>();
        for(String userName:configDao.getAllUserName()) {
            channelUserList.add(getChannelUser(userName));
        }
        return channelUserList;
    }

    public ChannelUser getChannelUser(String userName) {
        UserConfig userConfig = configDao.getUserConfig(userName);
        ChannelUser channelUser = new ChannelUser();
        channelUser.setUsername(userConfig.getUserName());
        channelUser.setPassword(userConfig.getPassword());
        channelUser.setChannel(channelServices.getChannel(userConfig.getChannel()));
        return channelUser;
    }

    public boolean checkUserPassword(String userName, String password) {
        try {
            ChannelUser channelUser = this.getChannelUser(userName);
            return password.equals(channelUser.getPassword());
        } catch (NullPointerException e) {
            return false;
        }
    }
}
