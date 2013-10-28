package net.alfss.smsserver.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.alfss.smsserver.config.GlobalConfig;
import net.alfss.smsserver.message.Message;
import net.alfss.smsserver.redis.exceptions.RedisUnknownError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;

import java.io.IOException;
import java.util.List;

/**
 * User: alfss
 * Date: 03.10.13
 * Time: 13:13
 */
public class RedisClient {
    final Logger logger = (Logger) LoggerFactory.getLogger(RedisClient.class);
    private GlobalConfig globalConfig;
    private JedisPool redisPoolConnect = null;

    public RedisClient(GlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
        connect();
    }

    public void connect() {
        logger.error("Create redis pool host = " + globalConfig.getRedisHost());
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxActive(globalConfig.getRedisMaxPool());
        redisPoolConnect = new JedisPool(poolConfig,
                                globalConfig.getRedisHost(),
                                globalConfig.getRedisPort(),
                                globalConfig.getRedisTimeOut(),
                                globalConfig.getRedisPassword(),
                                globalConfig.getRedisDatabase());

    }

    public void disconnect() {
        redisPoolConnect.destroy();
        logger.error("Destory redis pool host = " + globalConfig.getRedisHost());
    }
    public int getConnectTimeOutInMs() {
        return globalConfig.getRedisTimeOut() * 1000;
    }

    public void pushMessageToHeadList(Message message) throws JedisConnectionException, JedisDataException, RedisUnknownError {
        String listName = globalConfig.getChennelConfig(message.getChannel()).getChannelQueue();
        logger.debug("Write redis message to head list " + listName + " = " +  message.toString());
        rpushToList(listName, message.toString());
    }

    public void pushMessageToList(Message message) throws JedisConnectionException, JedisDataException, RedisUnknownError {
        String listName = globalConfig.getChennelConfig(message.getChannel()).getChannelQueue();
        logger.debug("Write redis message to list " + listName + " = " +  message.toString());
        lpushToList(listName, message.toString());
    }

    public void rpushToList(String listName, String value) throws JedisConnectionException, JedisDataException, RedisUnknownError {
        Jedis jedis = null;
        try {
            jedis = redisPoolConnect.getResource();
            jedis.rpush(listName, value);
        } catch (JedisDataException e) {
            throw e;
        } catch (JedisConnectionException e) {
            redisPoolConnect.returnBrokenResource(jedis);
            throw e;
        } catch (Exception e) {
            redisPoolConnect.returnBrokenResource(jedis);
            throw new RedisUnknownError(e.toString());
        } finally {
            if (jedis != null) redisPoolConnect.returnResource(jedis);
        }
    }

    public void lpushToList(String listName, String value) throws JedisConnectionException, JedisDataException, RedisUnknownError {
        Jedis jedis = null;
        try {
            jedis = redisPoolConnect.getResource();
            jedis.lpush(listName, value);
        } catch (JedisDataException e) {
            throw e;
        } catch (JedisConnectionException e) {
            redisPoolConnect.returnBrokenResource(jedis);
            throw e;
        } catch (Exception e) {
            redisPoolConnect.returnBrokenResource(jedis);
            throw new RedisUnknownError(e.toString());
        } finally {
            if (jedis != null) redisPoolConnect.returnResource(jedis);
        }
    }

    public Message popMessageFromList(String listName) throws JedisConnectionException, JedisDataException, RedisUnknownError {
        String value = rpopFromList(listName);
        if (value != null) {
            logger.debug("Read redis message from list " + listName + " = " + value);
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.readValue(value, Message.class);
            } catch (IOException e) {
                logger.error("Error read message redis" + e.toString());
            }
        }
        return null;
    }

    public String rpopFromList(String listName) throws JedisConnectionException, JedisDataException, RedisUnknownError {
        Jedis jedis = null;
        try {
            jedis = redisPoolConnect.getResource();
            return jedis.rpop(listName);
        } catch (JedisDataException e) {
            throw e;
        } catch (JedisConnectionException e) {
            redisPoolConnect.returnBrokenResource(jedis);
            throw e;
        } catch (Exception e) {
            redisPoolConnect.returnBrokenResource(jedis);
            throw new RedisUnknownError(e.toString());
        } finally {
            if (jedis != null) redisPoolConnect.returnResource(jedis);
        }
    }

    public Message popBlockMessageFromList(int timeOut, String listName) throws JedisConnectionException, JedisDataException, RedisUnknownError {
        String value = rbpopFromList(timeOut, listName);
        if (value != null) {
            logger.debug("Read(block read) redis message from list " + listName + " = " + value);
            ObjectMapper mapper = new ObjectMapper();
            try {
                return  mapper.readValue(value, Message.class);
            } catch (IOException e) {
                logger.error("Error read message redis" + e.toString());
            }
        }
        return null;
    }

    public String rbpopFromList(int timeOut, String listName) throws JedisConnectionException, JedisDataException, RedisUnknownError {
        Jedis jedis = null;
        try {
            jedis = redisPoolConnect.getResource();
            List<String> value = jedis.brpop(timeOut, listName);
            return value != null ? value.get(1) : null;
        } catch (JedisDataException e) {
            throw e;
        } catch (JedisConnectionException e) {
            redisPoolConnect.returnBrokenResource(jedis);
            throw e;
        } catch (Exception e) {
            redisPoolConnect.returnBrokenResource(jedis);
            throw new RedisUnknownError(e.toString());
        } finally {
            if (jedis != null) redisPoolConnect.returnResource(jedis);
        }
    }

    public Long getSizeList(String listName) throws JedisConnectionException, JedisDataException, RedisUnknownError {
        Jedis jedis = null;
        try {
            jedis = redisPoolConnect.getResource();
            return jedis.llen(listName);
        } catch (JedisDataException e) {
            throw e;
        } catch (JedisConnectionException e) {
            redisPoolConnect.returnBrokenResource(jedis);
            throw e;
        } catch (Exception e) {
            redisPoolConnect.returnBrokenResource(jedis);
            throw new RedisUnknownError(e.toString());
        } finally {
            if (jedis != null) redisPoolConnect.returnResource(jedis);
        }

    }

    public List<String> getElementsFromList(String listName, Long start, Long stop) throws JedisConnectionException, JedisDataException, RedisUnknownError {
        Jedis jedis = null;

        try {
            jedis = redisPoolConnect.getResource();
            return jedis.lrange(listName, start, stop);
        } catch (JedisDataException e) {
            throw e;
        } catch (JedisConnectionException e) {
            redisPoolConnect.returnBrokenResource(jedis);
            throw e;
        } catch (Exception e) {
            redisPoolConnect.returnBrokenResource(jedis);
            throw new RedisUnknownError(e.toString());
        } finally {
            if (jedis != null) redisPoolConnect.returnResource(jedis);
        }
    }
}
