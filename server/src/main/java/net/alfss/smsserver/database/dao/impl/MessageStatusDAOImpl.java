package net.alfss.smsserver.database.dao.impl;

import net.alfss.smsserver.database.dao.MessageStatusDAO;
import net.alfss.smsserver.database.entity.MessageStatus;
import net.alfss.smsserver.database.exceptions.DatabaseError;
import net.alfss.smsserver.utils.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.TransactionException;
import org.hibernate.criterion.Restrictions;

/**
 * User: alfss
 * Date: 19.03.14
 * Time: 16:43
 */
//TODO: нужно обрабатовать ошибки при падении коннекта!
public class MessageStatusDAOImpl implements MessageStatusDAO {
    @Override
    public MessageStatus get(int statusId) {
        Session session = getSession();
        try {
            session.beginTransaction();
            MessageStatus messageStatus = (MessageStatus) session.get(MessageStatus.class, statusId);
            session.getTransaction().commit();
            return messageStatus;
        } catch (TransactionException e) {
            session.getTransaction().rollback();
            throw e;
        } catch (RuntimeException e ) {
            session.getTransaction().rollback();
            throw new DatabaseError(e);
        }
    }

    @Override
    public MessageStatus getByName(String name) {
        Session session = getSession();
        try {
            session.beginTransaction();
            Criteria queryCriteria = session.createCriteria(MessageStatus.class)
                    .add(Restrictions.eq("name", name))
                    .setFirstResult(0);
            MessageStatus messageStatus =  (MessageStatus) queryCriteria.list().get(0);
            session.getTransaction().commit();
            return messageStatus;
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
