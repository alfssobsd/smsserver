package net.alfss.smsserver.http.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * User: alfss
 * Date: 18.10.13
 * Time: 13:45
 */
@Repository
public class RabbitMqDao {

    @Autowired
    private ConfigDao configDao;

    public RabbitMqDao() {

    }
}
