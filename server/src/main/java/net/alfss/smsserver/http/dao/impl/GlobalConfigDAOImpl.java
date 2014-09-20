package net.alfss.smsserver.http.dao.impl;

import net.alfss.smsserver.config.GlobalConfig;
import net.alfss.smsserver.config.SharedConfig;
import net.alfss.smsserver.http.dao.GlobalConfigDAO;
import org.springframework.stereotype.Repository;

/**
 * User: alfss
 * Date: 20.09.14
 * Time: 7:04
 */
@Repository
public class GlobalConfigDAOImpl implements GlobalConfigDAO {
    private GlobalConfig globalConfig;

    public GlobalConfigDAOImpl() {
        globalConfig = SharedConfig.getGlobalConfig();
    }

    @Override
    public String getJettyHttpToken() {
        return globalConfig.getJettyHttpToken();
    }
}
