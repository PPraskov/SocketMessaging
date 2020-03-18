package messaging.persistence.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users_table")
public class UserEntity extends Identification {

    private String username;
    private String password;
    private Set<MessageEntity> sentMessageEntities;
    private Set<MessageEntity> receivedMessageEntities;

    public UserEntity() {
    }

    @Column(name = "username", updatable = false, nullable = false, unique = true)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "password", nullable = false, length = 30)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @OneToMany(fetch = FetchType.LAZY,
            targetEntity = MessageEntity.class,
            mappedBy = "sender",
    cascade = CascadeType.REMOVE)
    public Set<MessageEntity> getSentMessageEntities() {
        return sentMessageEntities;
    }

    public void setSentMessageEntities(Set<MessageEntity> sentMessageEntities) {
        this.sentMessageEntities = sentMessageEntities;
    }

    @OneToMany(fetch = FetchType.LAZY,
            targetEntity = MessageEntity.class,
            mappedBy = "receiver",
            cascade = CascadeType.REMOVE)
    public Set<MessageEntity> getReceivedMessageEntities() {
        return receivedMessageEntities;
    }

    public void setReceivedMessageEntities(Set<MessageEntity> receivedMessageEntities) {
        this.receivedMessageEntities = receivedMessageEntities;
    }
}
