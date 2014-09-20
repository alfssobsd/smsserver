package net.alfss.smsserver.http.services;

import java.util.Map;

/**
 * User: alfss
 * Date: 20.09.14
 * Time: 2:37
 */
public interface ChannelControl {

    public boolean startChannel(String json, Map<String,String> header);
    public boolean stopChannel(String json, Map<String,String> header);
}
