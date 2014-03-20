package net.alfss.smsserver.sms.utils;

import net.alfss.smsserver.database.entity.Channel;
import net.alfss.smsserver.database.entity.Message;
import net.alfss.smsserver.sms.exceptions.SmsDestinationAddressWrongLength;
import net.alfss.smsserver.sms.exceptions.SmsSourceAddressWrongLength;
import org.smpp.Data;
import org.smpp.pdu.PDUException;
import org.smpp.pdu.SubmitSM;
import org.smpp.pdu.WrongLengthOfStringException;
import org.smpp.util.ByteBuffer;
import org.smpp.util.NotEnoughDataInByteBufferException;
import org.smpp.util.TerminatingZeroNotFoundException;

/**
 * User: alfss
 * Date: 29.11.13
 * Time: 14:55
 */
public class SubmitSmHelper {

    private final Channel channel;
    private final ShortMessageHelper shortMessageHelper;
    private final byte defaultDataCoding =  (byte) 0x08;

    public SubmitSmHelper(Channel channel, ShortMessageHelper shortMessageHelper) {
        this.channel = channel;
        this.shortMessageHelper = shortMessageHelper;
    }

    public SubmitSM createSubmitRequest(Message message) throws SmsDestinationAddressWrongLength, SmsSourceAddressWrongLength, TerminatingZeroNotFoundException, PDUException, NotEnoughDataInByteBufferException {

        SubmitSM request = new SubmitSM();
        request.setDataCoding(defaultDataCoding);
        try {
            if (message.getFrom() != null) {
                request.setSourceAddr(message.getFrom());
            } else {
                request.setSourceAddr(shortMessageHelper.createAddress(null));
            }
        } catch (WrongLengthOfStringException e) {
            throw new SmsSourceAddressWrongLength(e);
        }

        try {
            request.setDestAddr(shortMessageHelper.createAddress(message.getTo()));
        } catch (WrongLengthOfStringException e) {
            throw new SmsDestinationAddressWrongLength(e);
        }
        request.setSequenceNumber(message.getSequenceNumber());
        request.setEsmClass(message.getEsmClass());

        request.setRegisteredDelivery(Data.SM_SMSC_RECEIPT_REQUESTED);

        if (message.isPayload()) {
            request.setMessagePayload(new ByteBuffer(message.getMessageData()));
        } else {
            request.setShortMessageData(new ByteBuffer(message.getMessageData()));
        }

        return request;
    }


}
