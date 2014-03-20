package net.alfss.smsserver.sms.exceptions;

/**
 * User: alfss
 * Date: 26.11.13
 * Time: 4:09
 */
public class SmsServerException extends  RuntimeException {
    public SmsServerException(String message) {
        super(message);
    }

    public SmsServerException(Throwable e) {
        super(e);
    }

    public SmsServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
