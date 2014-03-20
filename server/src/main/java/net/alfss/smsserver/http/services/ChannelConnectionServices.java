package net.alfss.smsserver.http.services;

import net.alfss.smsserver.database.dao.impl.ChannelConnectionDAOImpl;
import net.alfss.smsserver.database.dao.impl.ChannelDAOImpl;
import net.alfss.smsserver.database.entity.Channel;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: alfss
 * Date: 20.02.14
 * Time: 1:44
 */
@Service
public class ChannelConnectionServices {

    final private ChannelConnectionDAOImpl channelConnectionDAO;
    final private ChannelDAOImpl channelDAO;

    public ChannelConnectionServices() {
        channelConnectionDAO = new ChannelConnectionDAOImpl();
        channelDAO = new ChannelDAOImpl();
    }


    public List getListByChannel(int channelId) {
        return getListByChannel(channelDAO.get(channelId));
    }

    public List getListByChannel(Channel channel) {
        return channelConnectionDAO.getList(channel);
    }
}
