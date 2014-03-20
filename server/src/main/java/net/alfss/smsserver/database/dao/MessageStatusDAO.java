package net.alfss.smsserver.database.dao;

import net.alfss.smsserver.database.entity.MessageStatus;

/**
 * User: alfss
 * Date: 19.03.14
 * Time: 16:41
 */
public interface MessageStatusDAO {
    public MessageStatus get(int statusId);
    public MessageStatus getByName(String name);
}
