package net.alfss.smsserver.sms.exceptions;

/**
 * User: alfss
 * Date: 24.10.13
 * Time: 19:00
 */
public class SmsServerNeedWait extends Exception {
    public SmsServerNeedWait() {}

    public SmsServerNeedWait(String message)
    {
        super(message);
    }

}
