package net.alfss.smsserver.sms.utils;

import net.alfss.smsserver.database.entity.Channel;
import net.alfss.smsserver.rabbit.data.InboundMessage;
import org.smpp.Data;
import org.smpp.pdu.Address;
import org.smpp.pdu.WrongLengthOfStringException;
import org.smpp.util.ByteBuffer;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * User: alfss
 * Date: 29.11.13
 * Time: 14:02
 */
public class ShortMessageHelper {

    private final Channel channel;
    private byte uniqIdMessage = (byte) 0x00;

    public  ShortMessageHelper(Channel channel) {
        this.channel = channel;
    }

    private byte getNextUniqIdMessage() {
        if (uniqIdMessage == (byte) 0x00) {
            uniqIdMessage++;
        }
        return uniqIdMessage++;
    }

    public Address createAddress(String address) throws WrongLengthOfStringException {
        Address addressInst = new Address();
        if (address == null) {
            addressInst.setTon((byte) channel.getSmppSourceTon()); // national ton
            addressInst.setNpi((byte) channel.getSmppSourceNpi()); // numeric plan indicator
            addressInst.setAddress(channel.getSmppSourceAddr(), Data.SM_ADDR_LEN);
        } else {
            addressInst.setTon((byte) channel.getSmppDestTon()); // national ton
            addressInst.setNpi((byte) channel.getSmppDestNpi()); // numeric plan indicator
            addressInst.setAddress(address, Data.SM_ADDR_LEN);
        }
        return addressInst;
    }

    public ByteBuffer[] getPrepareMessage(InboundMessage message, boolean enablePayLoad) throws UnsupportedEncodingException {
        ArrayList<String> list = new ArrayList<>();
        String str = message.getMessageText();
        if (message.getUniqueMessageNumber() == null) message.setUniqueMessageNumber((int) getNextUniqIdMessage());
        int maxMessages = channel.getSmppMaxSplitMessage();
        int countMessage = 0;

        if (enablePayLoad || str.length() < 70) {
            ByteBuffer[] arr = new ByteBuffer[1];
            ByteBuffer byteMessage = new ByteBuffer();
            byteMessage.appendString(str, Data.ENC_UTF16_BE);
            arr[0] = byteMessage;

            return arr;
        } else {
            while (str.length() > 67) {
                String s = str.substring(0, 67);
                str = str.substring(67);
                try {
                    if (countMessage < maxMessages) {
                        list.add(s);
                        countMessage++;
                    }
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
            }

            if (countMessage < maxMessages) {
                try {
                    list.add(str);
                } catch (Exception uce) {
                    uce.printStackTrace();
                }
            }

            ByteBuffer[] arr = new ByteBuffer[list.size()];

            if (list.size() == 1) {
                ByteBuffer byteMessage = new ByteBuffer();
                byteMessage.appendString(list.get(0), Data.ENC_UTF16_BE);
                arr[0] = byteMessage;

                return arr;
            }

            for (int i = 0; i < arr.length; i++) {
                ByteBuffer byteMessage = new ByteBuffer();
                byteMessage.appendByte((byte) 0x05);
                byteMessage.appendByte((byte) 0x00);
                byteMessage.appendByte((byte) 0x03);
                byteMessage.appendByte(Byte.valueOf(message.getUniqueMessageNumber().toString()));
                byteMessage.appendByte((byte) arr.length);
                byteMessage.appendByte((byte) (i + 1));
                byteMessage.appendString(list.get(i), Data.ENC_UTF16_BE);
                arr[i] = byteMessage;
            }

            return arr;
        }
    }
}
