package net.alfss.smsserver.utils;

import net.alfss.smsserver.config.GlobalConfig;
import net.alfss.smsserver.database.entity.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * User: alfss
 * Date: 05.12.13
 * Time: 18:32
 */
public class HibernateUtil
{

    private static SessionFactory sessionFactory;
    private static Configuration configuration;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            ServiceRegistryBuilder registry = new ServiceRegistryBuilder();
            registry.applySettings(configuration.getProperties());
            ServiceRegistry serviceRegistry = registry.buildServiceRegistry();

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }

        return sessionFactory;
    }

    public static void setupConnection(GlobalConfig config){
        configuration = new Configuration();
        configuration.addAnnotatedClass(Message.class);
        configuration.addAnnotatedClass(MessageStatus.class);
        configuration.addAnnotatedClass(Channel.class);
        configuration.addAnnotatedClass(ChannelConnection.class);
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        configuration.setProperty("hibernate.connection.url", config.getDbUrl());
        configuration.setProperty("hibernate.connection.username", config.getDbUserName());
        configuration.setProperty("hibernate.connection.password", config.getDbPassword());
        configuration.setProperty("hibernate.current_session_context_class", "thread");
        configuration.setProperty("hibernate.connection_pool_size", String.valueOf(config.getDbPoolSize()));
        configuration.setProperty("hibernate.hbm2ddl.auto", "none");
        configuration.setProperty("hibernate.show_sql", "false");
    }

}
