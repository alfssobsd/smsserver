package net.alfss.smsserver.database.dao.impl;

import net.alfss.smsserver.database.dao.UserDAO;
import net.alfss.smsserver.database.entity.User;
import net.alfss.smsserver.database.exceptions.DatabaseError;
import net.alfss.smsserver.utils.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import java.util.List;

/**
 * User: alfss
 * Date: 08.03.14
 * Time: 2:48
 */
public class UserDAOImpl implements UserDAO {


    @Override
    public void add(String userName, String password) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public User get(String userName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public User get(int userId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List getList() {
        Session session = getSession();
        try {
            session.beginTransaction();
            Criteria criteria = session.createCriteria(User.class)
                    .addOrder(Order.desc("userId"));
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
