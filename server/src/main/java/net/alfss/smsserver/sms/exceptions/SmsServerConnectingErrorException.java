package net.alfss.smsserver.sms.exceptions;

/**
 * User: alfss
 * Date: 10.10.13
 * Time: 14:39
 */
public class SmsServerConnectingErrorException extends Exception {
    public SmsServerConnectingErrorException() {}

    public SmsServerConnectingErrorException(String message)
    {
        super(message);
    }


}
