package client.messages;

import client.constants.MessageConstants;

import java.time.LocalDateTime;

public abstract class Message {
    private String id;
    private String from;
    private String auth;
    private String password;
    private String to;
    private String message;
    private String dateTimeAsString;

    public Message(){
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
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
