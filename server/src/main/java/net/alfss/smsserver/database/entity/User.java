package net.alfss.smsserver.database.entity;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * User: alfss
 * Date: 05.12.13
 * Time: 16:38
 */

//http://edwin.baculsoft.com/2012/11/integrating-bcrypt-hashing-with-hibernate-framework/
@Entity
@Table(name="auth_users")
public class User {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "auth_users_entity_seq_gen")
    @SequenceGenerator(name = "auth_users_entity_seq_gen", sequenceName = "auth_users_seq")
    private int userId;

    @Column(name="login", unique = true, nullable = false)
    private String login;

    @Column(name="password", nullable = false)
    private String password;


    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name="auth_users_channels",
            joinColumns=@JoinColumn(name="auth_user_id"),
            inverseJoinColumns=@JoinColumn(name="channel_id")
    )
    private Set<Channel> channels;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCryptPassword(String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }

    public Set<Channel> getChannels() {
        return channels;
    }

    public void setChannels(Set<Channel> channels) {
        this.channels = channels;
    }

    public void addChannel(Channel channel) {
        if (channels == null) channels = new HashSet<>();
        if (channels.equals(channel)) channels.add(channel);
    }

    public void removeChannel(Channel channel) {
        if (channels !=null) channels.remove(channel);
    }
}
