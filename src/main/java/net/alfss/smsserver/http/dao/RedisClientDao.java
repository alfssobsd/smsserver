package net.alfss.smsserver.http.dao;

import net.alfss.smsserver.config.SharedConfig;
import net.alfss.smsserver.message.Message;
import net.alfss.smsserver.redis.RedisClient;
import net.alfss.smsserver.redis.exceptions.RedisUnknownError;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * User: alfss
 * Date: 17.10.13
 * Time: 16:35
 */
@Repository
public class RedisClientDao {

    private RedisClient redisClient;

    public RedisClientDao() {
        this.redisClient = SharedConfig.getRedisClient();
    }

    public Long countMessageInChannel(String channelNameQueue) {
        try {
            return redisClient.getSizeList(channelNameQueue);
        } catch (RedisUnknownError redisUnknownError) {
            return (long) 0;
        }
    }

    public List<String> getMessageListFromChannel(String channelNameQueue, Long start, Long stop) {
        try {
            return redisClient.getElementsFromList(channelNameQueue, start, stop);
        } catch (RedisUnknownError redisUnknownError) {
            return new ArrayList<String>();
        }
    }

    public void pushMessageToChannel(Message message) {
        try {
            redisClient.pushMessageToList(message);
        } catch (RedisUnknownError redisUnknownError) {
            redisUnknownError.printStackTrace();
        }
    }
}
