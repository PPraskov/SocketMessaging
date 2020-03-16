package client.messages;

import client.constants.MessageConstants;

import java.time.LocalDateTime;

public abstract class Message {

    private String from;
    private String auth;
    private String to;
    private String message;
    private String dateTimeAsString;

    public Message(){
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getAuth() {
        return auth;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateTimeAsString() {
        return dateTimeAsString;
    }

    public void setDateTimeAsString(String dateTimeAsString) {
        this.dateTimeAsString = dateTimeAsString;
    }
}
