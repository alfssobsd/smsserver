package net.alfss.smsserver.sms.exceptions;

/**
 * User: alfss
 * Date: 29.11.13
 * Time: 15:03
 */
public class SmsSourceAddressWrongLength extends Exception {
    public SmsSourceAddressWrongLength(String message) {
        super(message);
    }

    public SmsSourceAddressWrongLength(Throwable e) {
        super(e);
    }

    public SmsSourceAddressWrongLength(String message, Throwable cause) {
        super(message, cause);
    }
}
