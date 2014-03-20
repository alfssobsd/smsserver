package net.alfss.smsserver.rabbit.exceptions;

/**
 * User: alfss
 * Date: 03.12.13
 * Time: 22:47
 */
public class RabbitMqConnectionException extends  RuntimeException {
    public RabbitMqConnectionException(String message) {
        super(message);
    }

    public RabbitMqConnectionException(Throwable e) {
        super(e);
    }

    public RabbitMqConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}