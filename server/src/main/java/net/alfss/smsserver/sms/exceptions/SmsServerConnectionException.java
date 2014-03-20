package net.alfss.smsserver.sms.exceptions;

/**
 * User: alfss
 * Date: 26.11.13
 * Time: 4:07
 */
public class SmsServerConnectionException extends SmsServerException {
    public SmsServerConnectionException(String message) {
        super(message);
    }

    public SmsServerConnectionException(Throwable e) {
        super(e);
    }

    public SmsServerConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

}
