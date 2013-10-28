package net.alfss.smsserver.config;

import net.alfss.smsserver.redis.RedisClient;

/**
 * User: alfss
 * Date: 17.10.13
 * Time: 12:38
 */
public class SharedConfig {

    private static GlobalConfig globalConfig;
    private static RedisClient redisClient;

    public SharedConfig() {

    }

    public synchronized static void setGlobalConfig(GlobalConfig globalConfig) {
        SharedConfig.globalConfig = globalConfig;
    }

    public synchronized static void setRedisClient(RedisClient redisClient) {
        SharedConfig.redisClient = redisClient;
    }

    public synchronized static GlobalConfig getGlobalConfig() {
        return globalConfig;
    }

    public synchronized static RedisClient getRedisClient() {
        return redisClient;
    }

}
