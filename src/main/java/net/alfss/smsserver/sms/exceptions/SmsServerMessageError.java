package net.alfss.smsserver.sms.exceptions;

/**
 * User: alfss
 * Date: 10.10.13
 * Time: 15:51
 */
public class SmsServerMessageError extends Exception {
    public SmsServerMessageError() {}

    public SmsServerMessageError(String message)
    {
        super(message);
    }

}
