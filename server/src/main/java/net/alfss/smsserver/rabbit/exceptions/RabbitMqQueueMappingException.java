package net.alfss.smsserver.rabbit.exceptions;

/**
 * User: alfss
 * Date: 17.12.13
 * Time: 3:04
 */
public class RabbitMqQueueMappingException extends  RuntimeException {
    public RabbitMqQueueMappingException(String message) {
        super(message);
    }

    public RabbitMqQueueMappingException(Throwable e) {
        super(e);
    }

    public RabbitMqQueueMappingException(String message, Throwable cause) {
        super(message, cause);
    }
}