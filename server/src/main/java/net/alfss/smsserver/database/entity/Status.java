package net.alfss.smsserver.database.entity;

import javax.persistence.*;
import java.util.List;

/**
 * User: alfss
 * Date: 18.12.13
 * Time: 14:31
 */
@Entity
@Table(name="status")
public class Status {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "status_entity_seq_gen")
    @SequenceGenerator(name = "status_entity_seq_gen", sequenceName = "status_seq")
    private int statusId;

    @Column(name="name", unique = true, nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status", cascade = CascadeType.ALL)
    public List<Message> messages;

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
