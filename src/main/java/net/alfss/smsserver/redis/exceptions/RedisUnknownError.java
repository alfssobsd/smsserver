package net.alfss.smsserver.redis.exceptions;

/**
 * User: alfss
 * Date: 22.10.13
 * Time: 14:51
 */
public class RedisUnknownError extends Exception {
    public RedisUnknownError() {}

    public RedisUnknownError(String message)
    {
        super(message);
    }
}
