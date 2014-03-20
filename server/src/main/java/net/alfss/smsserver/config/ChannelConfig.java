package net.alfss.smsserver.config;

import org.apache.commons.configuration.HierarchicalConfiguration;
import org.smpp.pdu.AddressRange;
import org.smpp.pdu.WrongLengthOfStringException;

/**
 * User: alfss
 * Date: 03.10.13
 * Time: 12:19
 */
public class ChannelConfig {
    private String channel;
    private String channelQueue;
    private String host;
    private int sendPort;
    private int receivePort;
    private String bindMode;
    private boolean enablePayLoad;
    private int maxMessage;
    private int enquireLinkInterval;
    private int waitMessageInterval;
    private String user;
    private String password;
    private int sourceAddrTon;
    private int sourceAddrNpi;
    private int destinationAddrTon;
    private int destinationAddrNpi;
    private String systemType;
    private int reconnectTimeOut;
    private int waitResendTimeOut;
    private String soureceAddress;
    private AddressRange addressRange;
    private int messagePerScond;
    private boolean fakeChannel;
    private boolean enableReceiptRequested;

    public ChannelConfig(HierarchicalConfiguration configuration) {
        addressRange = new AddressRange();
        setChannel(configuration.getString("channel"));
        setChannelQueue(configuration.getString("channel-queue", getChannel()));
        setHost(configuration.getString("host", "localhost"));
        setSendPort(configuration.getInt("send-port", 33333));
        setReceivePort(configuration.getInt("receive-port", 3333));
        setBindMode(configuration.getString("bind-mode", "t"));
        setEnablePayLoad(configuration.getBoolean("enable-payload", false));
        setUser(configuration.getString("username", "username"));
        setPassword(configuration.getString("password", "password"));
        setSourceAddrTon(configuration.getInt("source-addr-ton", 1));
        setSourceAddrNpi(configuration.getInt("source-addr-npi", 1));
        setDestinationAddrTon(configuration.getInt("dest-addr-ton", 1));
        setDestinationAddrNpi(configuration.getInt("dest-addr-npi", 1));
        setSystemType(configuration.getString("system-type"));
        setReconnectTimeOut(configuration.getInt("recconnect-timeout", 60));
        setEnquireLinkInterval(configuration.getInt("enquire-link-interval", 60));
        setWaitResendTimeOut(configuration.getInt("wait-resend-timeout", 5));
        setMaxMessage(configuration.getInt("max-message", 10));
        setSoureceAddress(configuration.getString("source-addr", ""));
        setAddressRange(configuration.getString("address-range", addressRange.getAddressRange()));
        setMessagePerScond(configuration.getInt("message-per-second", 40));
        setFakeChannel(configuration.getBoolean("is-fake-channel", false));
        setEnableReceiptRequested(configuration.getBoolean("is-receipt-requested", true));
        setWaitMessageInterval(configuration.getInt("wait-message-interval", 300));
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getSendPort() {
        return sendPort;
    }

    public void setSendPort(int sendPort) {
        this.sendPort = sendPort;
    }

    public String getBindMode() {
        return bindMode;
    }

    public void setBindMode(String bindMode) {
        this.bindMode = bindMode;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getSourceAddrTon() {
        return sourceAddrTon;
    }

    public void setSourceAddrTon(int sourceAddrTon) {
        this.sourceAddrTon = sourceAddrTon;
    }

    public int getSourceAddrNpi() {
        return sourceAddrNpi;
    }

    public void setSourceAddrNpi(int sourceAddrNpi) {
        this.sourceAddrNpi = sourceAddrNpi;
    }

    public int getDestinationAddrTon() {
        return destinationAddrTon;
    }

    public void setDestinationAddrTon(int destinationAddrTon) {
        this.destinationAddrTon = destinationAddrTon;
    }

    public int getDestinationAddrNpi() {
        return destinationAddrNpi;
    }

    public void setDestinationAddrNpi(int destinationAddrNpi) {
        this.destinationAddrNpi = destinationAddrNpi;
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public int getReconnectTimeOut() {
        return reconnectTimeOut;
    }

    public void setReconnectTimeOut(int reconnectTimeOut) {
        this.reconnectTimeOut = reconnectTimeOut;
    }

    public String getSoureceAddress() {
        return soureceAddress;
    }

    public void setSoureceAddress(String soureceAddress) {
        this.soureceAddress = soureceAddress;
    }

    public int getReceivePort() {
        return receivePort;
    }

    public void setReceivePort(int receivePort) {
        this.receivePort = receivePort;
    }

    public AddressRange getAddressRange() {
        return addressRange;
    }

    public void setAddressRange(String addressRange) {
        try {
            this.addressRange.setAddressRange(addressRange);
        } catch (WrongLengthOfStringException e) {
            this.addressRange = new AddressRange();
        }
    }

    public boolean getEnablePayLoad() {
        return enablePayLoad;
    }

    public void setEnablePayLoad(boolean enablePayLoad) {
        this.enablePayLoad = enablePayLoad;
    }

    public int getMaxMessage() {
        return maxMessage;
    }

    public void setMaxMessage(int maxMessage) {
        this.maxMessage = maxMessage;
    }

    public int getEnquireLinkInterval() {
        return enquireLinkInterval;
    }

    public int getEnquireLinkIntervalInMs() {
        return enquireLinkInterval * 1000;
    }

    public void setEnquireLinkInterval(int enquireLinkInterval) {
        this.enquireLinkInterval = enquireLinkInterval;
    }

    public String getChannelQueue() {
        return channelQueue;
    }

    public void setChannelQueue(String channelQueue) {
        this.channelQueue = "smsserver_" + channelQueue;
    }

    public String getChannelQueuePrepare() {
        return "smsserver_" + getChannel() + "_prepare_message";
    }

    public int getMessagePerScond() {
        return messagePerScond;
    }

    public void setMessagePerScond(int messagePerScond) {
        this.messagePerScond = messagePerScond;
    }

    public boolean isFakeChannel() {
        return fakeChannel;
    }

    public void setFakeChannel(boolean fakeChannel) {
        this.fakeChannel = fakeChannel;
    }

    public int getWaitResendTimeOut() {
        return waitResendTimeOut;
    }

    public void setWaitResendTimeOut(int waitResendTimeOut) {
        this.waitResendTimeOut = waitResendTimeOut;
    }

    public boolean isEnableReceiptRequested() {
        return enableReceiptRequested;
    }

    public void setEnableReceiptRequested(boolean enableReceiptRequested) {
        this.enableReceiptRequested = enableReceiptRequested;
    }

    public int getWaitMessageInterval() {
        return waitMessageInterval;
    }

    public int getWaitMessageIntervalMs() {
        return waitMessageInterval * 1000;
    }

    public void setWaitMessageInterval(int waitMessageInterval) {
        this.waitMessageInterval = waitMessageInterval;
    }
}
