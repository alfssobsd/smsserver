package net.alfss.smsserver.http.domain;

/**
 * User: alfss
 * Date: 17.10.13
 * Time: 13:34
 */
public class ChannelUser {
    private String username;
    private String password;
    private Channel channel;

    public ChannelUser() {

    }

    public ChannelUser(String username, String password, Channel channel) {
        this.username = username;
        this.password = password;
        this.channel = channel;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
