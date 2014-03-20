package net.alfss.smsserver.database.dao.impl;

import net.alfss.smsserver.database.dao.StatusDAO;
import net.alfss.smsserver.database.entity.Status;
import net.alfss.smsserver.database.exceptions.DatabaseError;
import net.alfss.smsserver.utils.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * User: alfss
 * Date: 19.03.14
 * Time: 16:43
 */
public class StatusDAOImpl  implements StatusDAO {
    @Override
    public Status get(int statusId) {
        Session session = getSession();
        try {
            session.beginTransaction();
            Status status = (Status) session.get(Status.class, statusId);
            session.getTransaction().commit();
            return status;
        } catch (RuntimeException e ) {
            session.getTransaction().rollback();
            throw new DatabaseError(e);
        }
    }

    @Override
    public Status getByName(String name) {
        Session session = getSession();
        try {
            session.beginTransaction();
            Criteria queryCriteria = session.createCriteria(Status.class)
                    .add(Restrictions.eq("name", name))
                    .setFirstResult(0);
            Status status =  (Status) queryCriteria.list().get(0);
            session.getTransaction().commit();
            return status;
        } catch (RuntimeException e ) {
            session.getTransaction().rollback();
            throw new DatabaseError(e);
        }
    }

    private Session getSession() {
        return HibernateUtil.getSessionFactory().getCurrentSession();
    }
}
