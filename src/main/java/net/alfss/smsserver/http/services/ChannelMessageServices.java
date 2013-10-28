package net.alfss.smsserver.http.services;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.alfss.smsserver.http.dao.ConfigDao;
import net.alfss.smsserver.http.dao.RedisClientDao;
import net.alfss.smsserver.http.domain.Channel;
import net.alfss.smsserver.http.domain.ChannelMessage;
import net.alfss.smsserver.http.domain.ChannelUser;
import net.alfss.smsserver.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: alfss
 * Date: 18.10.13
 * Time: 17:04
 */

@Service
public class ChannelMessageServices {
    private RedisClientDao redisClientDao;
    private ConfigDao configDao;

    @Autowired
    public void setChannelDao(RedisClientDao redisClientDao, ConfigDao configDao) {
        this.redisClientDao = redisClientDao;
        this.configDao = configDao;
    }

    public void pushMessageToChannel(String phone, String messageText, String channelName) {
        Message message = new Message();
        message.setPhone(phone);
        message.setMessageText(messageText);
        message.setChannel(channelName);
        message.setPriority(1);
        redisClientDao.pushMessageToChannel(message);
    }

    public boolean pushJsonMessageToChannel(String json, ChannelUser channelUser) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Message message = mapper.readValue(json, Message.class);
            message.setChannel(channelUser.getChannel().getChannelName());
            redisClientDao.pushMessageToChannel(message);
        } catch (JsonMappingException e) {
            return false;
        } catch (JsonParseException e) {
            return false;
        } catch (IOException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

    public List<ChannelMessage> getAllMessageFromChannel(Channel channel) {
        List<ChannelMessage> messageList = new ArrayList<ChannelMessage>();

        for(String messageRaw: redisClientDao.getMessageListFromChannel(channel.getChannelQueue(), (long) 0,
                redisClientDao.countMessageInChannel(channel.getChannelQueue()))) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                messageList.add(new ChannelMessage(mapper.readValue(messageRaw, Message.class)));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return messageList;
    }


    public  List<ChannelMessage> getMessageListFromChannel(Channel channel, long step) {
        List<ChannelMessage> messageList = new ArrayList<ChannelMessage>();

        Long start = step * 200;
        Long stop = start + 200;

        for(String messageRaw: redisClientDao.getMessageListFromChannel(channel.getChannelQueue(), start, stop)) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                messageList.add(new ChannelMessage(mapper.readValue(messageRaw, Message.class)));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return messageList;
    }


}
