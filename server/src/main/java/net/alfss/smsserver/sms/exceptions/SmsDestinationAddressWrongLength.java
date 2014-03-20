package net.alfss.smsserver.sms.exceptions;

/**
 * User: alfss
 * Date: 29.11.13
 * Time: 15:04
 */
public class SmsDestinationAddressWrongLength extends Exception {
    public SmsDestinationAddressWrongLength(String message) {
        super(message);
    }

    public SmsDestinationAddressWrongLength(Throwable e) {
        super(e);
    }

    public SmsDestinationAddressWrongLength(String message, Throwable cause) {
        super(message, cause);
    }
}