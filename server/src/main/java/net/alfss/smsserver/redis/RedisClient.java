package net.alfss.smsserver.redis;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.alfss.smsserver.config.GlobalConfig;
import net.alfss.smsserver.rabbit.data.InboundMessage;
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

    public String getKey(String key) throws JedisConnectionException, JedisDataException, RedisUnknownError {
        Jedis jedis = null;

        try {
            jedis = redisPoolConnect.getResource();
            return jedis.get(key);
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

    public void setKey(String key, String value, int expireTimeMs) throws JedisConnectionException, JedisDataException, RedisUnknownError {
        Jedis jedis = null;

        try {
            jedis = redisPoolConnect.getResource();
            jedis.psetex(key, expireTimeMs, value);
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


    // # SET/GET KEY InboundMessage

    public void setKeyInboundMessage(String key, InboundMessage message, int expireTime) throws  JedisConnectionException, JedisDataException, RedisUnknownError {
        setKey(key, message.toString(), expireTime);
    }

    public InboundMessage getKeyInboundMessage(String key) throws JedisConnectionException, JedisDataException, RedisUnknownError {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return  mapper.readValue(getKey(key), InboundMessage.class);
        } catch (IOException e) {
            logger.error("Error mapping internal message " + e.toString());
        }
        return null;
    }

    // #- SET/GET KEY InboundMessage

    // # SET/GET KEY Message
    public void setKeyMessage(String key, InboundMessage message, int expireTime) throws JedisConnectionException, JedisDataException, RedisUnknownError {
        setKey(key, message.toString(), expireTime);
    }

    public InboundMessage getKeyMessage(String key) throws JedisConnectionException, JedisDataException, RedisUnknownError {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return  mapper.readValue(getKey(key), InboundMessage.class);
        } catch (IOException e) {
            logger.error("Error mapping message " + e.toString());
        }
        return null;
    }

    // #- SET/GET KEY Message

    // # List InboundMessage or Message
    public void pushMessageToHeadList(InboundMessage message) throws JedisConnectionException, JedisDataException, RedisUnknownError {
        String listName = "test";
        logger.debug("Write redis message to head list " + listName + " = " +  message.toString());
        rpushToList(listName, message.toString());
    }

    public void pushMessageToList(InboundMessage message) throws JedisConnectionException, JedisDataException, RedisUnknownError {
        String listName = "test";
        logger.debug("Write redis message to list " + listName + " = " +  message.toString());
        lpushToList(listName, message.toString());
    }

    public void pushInboundMessageToHeadList(InboundMessage message, String queue) throws JedisConnectionException, JedisDataException, RedisUnknownError {
        logger.debug("Write redis message to head list " + queue + " = " +  message.toString());
        rpushToList(queue, message.toString());
    }

    public void pushInboundMessageToList(InboundMessage message, String queue) throws JedisConnectionException, JedisDataException, RedisUnknownError {
        logger.debug("Write redis message to list " + queue + " = " +  message.toString());
        lpushToList(queue, message.toString());
    }

    public InboundMessage popMessageFromList(String listName) throws JedisConnectionException, JedisDataException, JsonMappingException, JsonParseException, RedisUnknownError {
        String value = rpopFromList(listName);
        if (value != null) {
            logger.debug("Read redis message from list " + listName + " = " + value);
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.readValue(value, InboundMessage.class);
            } catch (IOException e) {
                logger.error("Error read message redis" + e.toString());
            }
        }
        return null;
    }

    public InboundMessage popBlockMessageFromList(int timeOut, String listName) throws JedisConnectionException, JedisDataException, RedisUnknownError, JsonMappingException, JsonParseException {
        String value = rbpopFromList(timeOut, listName);
        if (value != null) {
            logger.debug("Read(block read) redis message from list " + listName + " = " + value);
            ObjectMapper mapper = new ObjectMapper();
            try {
                return  mapper.readValue(value, InboundMessage.class);
            } catch (IOException e) {
                logger.error("Error read message redis" + e.toString());
            }
        }
        return null;
    }

    public InboundMessage popBlockInboundMessageFromList(int timeOut, String listName) throws JedisConnectionException, JedisDataException, RedisUnknownError, JsonMappingException, JsonParseException {
        String value = rbpopFromList(timeOut, listName);
        if (value != null) {
            logger.debug("Read(block read) redis message from list " + listName + " = " + value);
            ObjectMapper mapper = new ObjectMapper();
            try {
                return  mapper.readValue(value, InboundMessage.class);
            } catch (IOException e) {
                logger.error("Error read message redis" + e.toString());
            }
        }
        return null;
    }
    // #- List InboundMessage or Message
}
