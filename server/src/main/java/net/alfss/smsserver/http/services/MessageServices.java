package net.alfss.smsserver.http.services;

import net.alfss.smsserver.database.dao.impl.ChannelDAOImpl;
import net.alfss.smsserver.database.dao.impl.MessageDAOImpl;
import net.alfss.smsserver.database.entity.Channel;
import net.alfss.smsserver.database.entity.Message;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: alfss
 * Date: 19.02.14
 * Time: 13:03
 */

@Service
public class MessageServices {

    final private MessageDAOImpl messageDAO;
    final private ChannelDAOImpl channelDAO;

    public MessageServices() {
        messageDAO = new MessageDAOImpl();
        channelDAO = new ChannelDAOImpl();
    }

    public List getMessageListByChannel(int channelId, int limit, int offset) {
        return getMessageListByChannel(channelDAO.get(channelId), limit, offset);
    }

    public List getMessageListByChannel(Channel channel, int limit, int offset) {
        return  messageDAO.getList(channel, limit, offset);
    }

    public Message getMessage(int id) {
        return  messageDAO.get(id);
    }
}
