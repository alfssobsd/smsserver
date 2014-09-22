package net.alfss.smsserver.http.services;

import java.util.Map;

/**
 * User: alfss
 * Date: 20.09.14
 * Time: 2:37
 */
public interface ChannelControl {

    public String startChannel(String json, Map<String,String> header);
    public String stopChannel(String json, Map<String,String> header);
}
