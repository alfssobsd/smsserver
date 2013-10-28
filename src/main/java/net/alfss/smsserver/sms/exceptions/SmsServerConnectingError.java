package net.alfss.smsserver.sms.exceptions;

/**
 * User: alfss
 * Date: 10.10.13
 * Time: 14:39
 */
public class SmsServerConnectingError extends Exception {
    public SmsServerConnectingError() {}

    public SmsServerConnectingError(String message)
    {
        super(message);
    }

}
