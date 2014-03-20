package net.alfss.smsserver.database.dao.impl;

import net.alfss.smsserver.database.dao.ChannelDAO;
import net.alfss.smsserver.database.entity.Channel;
import net.alfss.smsserver.database.exceptions.DatabaseError;
import net.alfss.smsserver.utils.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * User: alfss
 * Date: 09.12.13
 * Time: 16:50
 */
public class ChannelDAOImpl implements ChannelDAO {

    @Override
    public Channel get(int channelId) {
        Session session = getSession();
        try {
            session.beginTransaction();
            Channel channel = (Channel) session.get(Channel.class, channelId);
            session.getTransaction().commit();
            return channel;
        } catch (RuntimeException e ) {
            session.getTransaction().rollback();
            throw new DatabaseError(e);
        }
    }

    @Override
    public void create(Channel channel) {
        Session session = getSession();
        try {
            session.beginTransaction();
            session.save(channel);
            session.getTransaction().commit();
        } catch (RuntimeException e ) {
            session.getTransaction().rollback();
            throw new DatabaseError(e);
        }
    }

    @Override
    public List getAllEnableList() {
        Session session = getSession();
        try {
            session.beginTransaction();
            Criteria criteria = session.createCriteria(Channel.class)
                    .add(Restrictions.eq("enable", true));
            List list =  criteria.list();
            session.getTransaction().commit();
            return list;
        } catch (RuntimeException e ) {
            session.getTransaction().rollback();
            throw new DatabaseError(e);
        }
    }

    private Session getSession() {
        return HibernateUtil.getSessionFactory().getCurrentSession();
    }
}
