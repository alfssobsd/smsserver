package net.alfss.smsserver.sms.pool;


import net.alfss.smsserver.config.GlobalConfig;
import net.alfss.smsserver.database.entity.Channel;
import net.alfss.smsserver.database.entity.ChannelConnection;
import net.alfss.smsserver.sms.AsyncSmsServerEventListener;
import net.alfss.smsserver.sms.exceptions.SmsServerConnectionException;
import net.alfss.smsserver.sms.prototype.Pool;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smpp.Connection;
import org.smpp.Data;
import org.smpp.Session;
import org.smpp.TCPIPConnection;
import org.smpp.pdu.BindTransciever;
import org.smpp.pdu.Response;

/**
 * User: alfss
 * Date: 26.11.13
 * Time: 3:49
 */
public class SmsServerConnectPool extends Pool<Session> {
    final Logger logger = (Logger) LoggerFactory.getLogger(SmsServerConnectPool.class);

    public SmsServerConnectPool(final GenericObjectPoolConfig poolConfig, GlobalConfig config, final Channel channel, final ChannelConnection channelConnection) {
        super(poolConfig, new SmsServerFactory(config ,channel, channelConnection));
    }

    private static class SmsServerFactory extends BasePooledObjectFactory<Session> {
        final Logger logger = (Logger) LoggerFactory.getLogger(SmsServerFactory.class);

        private final Channel channel;
        private final ChannelConnection channelConnection;
        private final GlobalConfig config;

        public SmsServerFactory(GlobalConfig config, Channel channel, ChannelConnection channelConnection) {
            this.channel = channel;
            this.channelConnection = channelConnection;
            this.config = config;
        }

        @Override
        public Session create() throws Exception {
            logger.error("SmsServerFactory: create connect to " + channel.getSmppHost() + " channel = " + channel.getName());
            Connection conn = new TCPIPConnection(channel.getSmppHost(), channel.getSmppPort());
            conn.setReceiveTimeout(20 * 1000);
            Session session = new Session(conn);
            BindTransciever bindRequest = new BindTransciever();
            bindRequest.setSystemId(channel.getSmppUserName());
            bindRequest.setPassword(channel.getSmppPassword());
            bindRequest.setSystemType(channelConnection.getSmppSystemType());
            bindRequest.setAddressRange("");
            AsyncSmsServerEventListener pduListener = new AsyncSmsServerEventListener(config, channel);
            Response response = session.bind(bindRequest, pduListener);
            if(response.getCommandStatus() != Data.ESME_ROK) {
                throw  new SmsServerConnectionException("Error connect STATUS = " + response.getCommandStatus());
            }
            return session;
        }

        @Override
        public PooledObject<Session> wrap(Session o) {
            return new DefaultPooledObject<>(o);
        }

        @Override
        public void destroyObject(PooledObject pooledObject) throws Exception {
            Session session = (Session) pooledObject.getObject();
            if(session.isOpened()) {
                session.unbind();
            }
        }

        @Override
        public boolean validateObject(PooledObject pooledObject) {
            Session session = (Session) pooledObject.getObject();
            return session.isOpened();
        }

    }
}