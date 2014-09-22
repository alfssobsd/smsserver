package net.alfss.smsserver.database.dao.impl;

import net.alfss.smsserver.database.dao.ChannelConnectionDAO;
import net.alfss.smsserver.database.entity.Channel;
import net.alfss.smsserver.database.entity.ChannelConnection;
import net.alfss.smsserver.database.exceptions.DatabaseError;
import net.alfss.smsserver.utils.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.TransactionException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * User: alfss
 * Date: 15.02.14
 * Time: 4:32
 */
//TODO: нужно обрабатовать ошибки при падении коннекта!
//org.hibernate.TransactionException
//Caused by: org.hibernate.TransactionException: unable to rollback against JDBC connection
public class ChannelConnectionDAOImpl implements ChannelConnectionDAO {
    @Override
    public ChannelConnection get(int channelConnectionId) {
        Session session = getSession();
        try {
            session.beginTransaction();
            ChannelConnection channelConnection = (ChannelConnection) session.get(ChannelConnection.class, channelConnectionId);
            session.getTransaction().commit();
            return channelConnection;
        } catch (TransactionException e) {
            session.getTransaction().rollback();
            throw e;
        } catch (RuntimeException e ) {
            session.getTransaction().rollback();
            throw new DatabaseError(e);
        }
    }

    @Override
    public List getList(int channelId) {
        ChannelDAOImpl channelDAO = new ChannelDAOImpl();
        return getList(channelDAO.get(channelId));
    }

    @Override
    public List getList(Channel channel) {

        Session session = getSession();
        try {
            session.beginTransaction();
            Criteria criteria = session.createCriteria(ChannelConnection.class)
                    .add(Restrictions.eq("channel", channel))
                    .addOrder(Order.desc("channeConnectionlId"));
            List list = criteria.list();
            session.getTransaction().commit();
            return list;
        } catch (TransactionException e) {
            session.getTransaction().rollback();
            throw e;
        } catch (RuntimeException e ) {
            session.getTransaction().rollback();
            throw new DatabaseError(e);
        }
    }

    private Session getSession() {
        return HibernateUtil.getSessionFactory().getCurrentSession();
    }
}
