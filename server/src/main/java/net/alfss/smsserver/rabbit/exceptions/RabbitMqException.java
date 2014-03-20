package net.alfss.smsserver.rabbit.exceptions;

/**
 * User: alfss
 * Date: 03.12.13
 * Time: 22:46
 */
public class RabbitMqException extends  RuntimeException {
    public RabbitMqException(String message) {
        super(message);
    }

    public RabbitMqException(Throwable e) {
        super(e);
    }

    public RabbitMqException(String message, Throwable cause) {
        super(message, cause);
    }
}