package net.alfss.smsserver.rabbit.pool;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import net.alfss.smsserver.config.GlobalConfig;
import net.alfss.smsserver.rabbit.prototype.Pool;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: alfss
 * Date: 26.11.13
 * Time: 3:49
 */
public class RabbitMqPool extends Pool<Channel> {
    final Logger logger = (Logger) LoggerFactory.getLogger(RabbitMqPool.class);

    public RabbitMqPool(final GenericObjectPoolConfig poolConfig, final GlobalConfig config) {
        super(poolConfig, new SmsServerFactory(config));
    }

    private static class SmsServerFactory extends BasePooledObjectFactory<Channel> {
        final Logger logger = (Logger) LoggerFactory.getLogger(SmsServerFactory.class);

        private final GlobalConfig config;

        public SmsServerFactory(GlobalConfig config) {
            this.config = config;
        }

        @Override
        public Channel create() throws Exception {
            logger.error("RabbitMqPool: create connect to " + config.getRabbitHost() + " vhost = " + config.getRabbitVhost());
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUsername(config.getRabbitUser());
            factory.setPassword(config.getRabbitPassword());
            factory.setVirtualHost(config.getRabbitVhost());
            factory.setHost(config.getRabbitHost());
            factory.setPort(config.getRabbitPort());
            factory.setConnectionTimeout(config.getRabbitConnectTimeOut());
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.basicQos(0, 50, false);
            return channel;
        }

        @Override
        public PooledObject<Channel> wrap(Channel o) {
            return new DefaultPooledObject<>(o);
        }

        @Override
        public void destroyObject(PooledObject pooledObject) throws Exception {
            Channel channel = (Channel) pooledObject.getObject();
            if(channel.isOpen()) {
                Connection connection = channel.getConnection();
                channel.close();
                if(connection.isOpen()) connection.close();
            }
        }

        @Override
        public boolean validateObject(PooledObject pooledObject) {
            Channel channel = (Channel) pooledObject.getObject();
            return channel.isOpen();
        }

    }
}
