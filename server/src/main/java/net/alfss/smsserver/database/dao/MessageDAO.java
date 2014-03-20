package net.alfss.smsserver.database.dao;

import net.alfss.smsserver.database.entity.Channel;
import net.alfss.smsserver.database.entity.Message;

import java.util.List;

/**
 * User: alfss
 * Date: 07.12.13
 * Time: 8:19
 */
public interface MessageDAO {
    public Message get(int messageId);
    public int create(Message message);
    public void create(Message message, Channel channel);
    public void update(Message message);
    public Message getWaitResponse(int sequenceNumber, Channel channel);
    public Message getWaiteDelivery(int messageSmsId, String queue);
    public Message getWaiteDelivery(int messageSmsId, Channel channel);
    public void setStatusFail(Message message);
    public void setStatusFail(int messageId);
    public void setStatusSuccess(Message message);
    public void setStatusSuccess(int messageId);
    public void setStatusWaitSend(Message message);
    public void setStatusWaitSend(int messageId);
    public void setStatusWaitResponse(Message message);
    public void setStatusWaitResponse(int messageId);
    public void setStatusWaiteDelivery(Message message);
    public void setStatusWaiteDelivery(int messageId);
    public List getList(Channel channel, int limit, int offset);
}
