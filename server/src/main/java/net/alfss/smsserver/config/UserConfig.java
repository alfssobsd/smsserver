package net.alfss.smsserver.config;

import org.apache.commons.configuration.HierarchicalConfiguration;

/**
 * User: alfss
 * Date: 17.10.13
 * Time: 17:58
 */
public class UserConfig {
    private String userName;
    private String password;
    private String channel;

    public UserConfig(HierarchicalConfiguration configuration) {
        setChannel(configuration.getString("channel"));
        setUserName(configuration.getString("username"));
        setPassword(configuration.getString("password"));
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
