package net.alfss.smsserver.rabbit.prototype;

import net.alfss.smsserver.rabbit.exceptions.RabbitMqConnectionException;
import net.alfss.smsserver.rabbit.exceptions.RabbitMqException;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * User: alfss
 * Date: 26.11.13
 * Time: 4:04
 */
public abstract class Pool<T> {

    protected GenericObjectPool internalPool;

    @SuppressWarnings("unchecked")
    public Pool(GenericObjectPoolConfig poolConfig, PooledObjectFactory factory) {
        this.internalPool = new GenericObjectPool(factory, poolConfig);
    }

    @SuppressWarnings("unchecked")
    public T getResource() {
        try {
            return (T) internalPool.borrowObject();
        } catch (Exception e) {
            throw new RabbitMqConnectionException(
                    "Could not get a resource from the pool", e);
        }
    }

    @SuppressWarnings("unchecked")
    public void returnResourceObject(final Object resource) {
        try {
            internalPool.returnObject(resource);
        } catch (Exception e) {
            throw new RabbitMqException(
                    "Could not return the resource to the pool", e);
        }
    }

    public void returnBrokenResource(final T resource) {
        returnBrokenResourceObject(resource);
    }

    public void returnResource(final T resource) {
        returnResourceObject(resource);
    }

    @SuppressWarnings("unchecked")
    protected void returnBrokenResourceObject(final Object resource) {
        try {
            internalPool.invalidateObject(resource);
        } catch (Exception e) {
            throw new RabbitMqException(
                    "Could not return the resource to the pool", e);
        }
    }

    public void destroy() {
        try {
            internalPool.close();
        } catch (Exception e) {
            throw new RabbitMqException("Could not destroy the pool", e);
        }
    }
}
