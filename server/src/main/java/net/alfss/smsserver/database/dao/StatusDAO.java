package net.alfss.smsserver.database.dao;

import net.alfss.smsserver.database.entity.Status;

/**
 * User: alfss
 * Date: 19.03.14
 * Time: 16:41
 */
public interface StatusDAO {
    public Status get(int statusId);
    public Status getByName(String name);
}
