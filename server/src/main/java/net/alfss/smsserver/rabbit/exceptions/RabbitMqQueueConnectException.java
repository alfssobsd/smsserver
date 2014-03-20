package net.alfss.smsserver.rabbit.exceptions;

/**
 * User: alfss
 * Date: 17.12.13
 * Time: 2:47
 */
public class RabbitMqQueueConnectException extends  RuntimeException {
    public RabbitMqQueueConnectException(String message) {
        super(message);
    }

    public RabbitMqQueueConnectException(Throwable e) {
        super(e);
    }

    public RabbitMqQueueConnectException(String message, Throwable cause) {
        super(message, cause);
    }
}