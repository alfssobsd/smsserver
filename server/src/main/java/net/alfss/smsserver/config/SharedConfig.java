package net.alfss.smsserver.config;

/**
 * User: alfss
 * Date: 17.10.13
 * Time: 12:38
 */
public class SharedConfig {

    private static GlobalConfig globalConfig;

    public SharedConfig() { }

    public synchronized static void setGlobalConfig(GlobalConfig globalConfig) {
        SharedConfig.globalConfig = globalConfig;
    }

    public synchronized static GlobalConfig getGlobalConfig() {
        return globalConfig;
    }

}
