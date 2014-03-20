package net.alfss.smsserver.database.dao;

import net.alfss.smsserver.database.entity.User;

import java.util.List;

/**
 * User: alfss
 * Date: 05.12.13
 * Time: 16:33
 */
public interface UserDAO {
    public void add(String userName, String password);
    public User get(String userName);
    public User get(int userId);
    public List getList();
}
