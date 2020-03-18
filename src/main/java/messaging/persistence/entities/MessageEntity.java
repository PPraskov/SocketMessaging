package messaging.persistence.entities;

import messaging.constants.MessageConstants;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages_table")
public class MessageEntity extends Identification implements AttributeConverter<LocalDateTime, Timestamp> {

    private UserEntity sender;
    private UserEntity receiver;
    private String message;
    private LocalDateTime dateTime;

    public MessageEntity() {
    }

    @ManyToOne(targetEntity = UserEntity.class,fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    public UserEntity getSender() {
        return sender;
    }

    public void setSender(UserEntity sender) {
        this.sender = sender;
    }

    @ManyToOne(targetEntity = UserEntity.class,fetch = FetchType.EAGER)
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    public UserEntity getReceiver() {
        return receiver;
    }

    public void setReceiver(UserEntity receiver) {
        this.receiver = receiver;
    }

    @Column(name = "message_content", nullable = false, length = 4000)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Column(name = "message_sent_time", nullable = false)
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime localDateTime) {
        return localDateTime == null ? null : Timestamp.valueOf(localDateTime);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }

    public void setDateTimeAsString(String dateTimeAsString) {
        if (dateTimeAsString != null){
            this.dateTime = LocalDateTime.parse(dateTimeAsString, MessageConstants.dtf);

        }
    }
    @Transient
    public String getDateTimeAsString() {
        return MessageConstants.dtf.format(this.dateTime);
    }
}
