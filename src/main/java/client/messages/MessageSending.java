package client.messages;

import client.User;

import java.lang.reflect.InvocationTargetException;

public class MessageSending extends OutputMessage {

    private final String from;
    private final String auth;
    private final String to;
    private final String message;
    private String sendDateTimeAsString;

   public MessageSending(User user, String to, String message) {
        this.from = user.getName();
        this.auth = user.getAuth();
        this.to = to;
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public String getAuth() {
        return auth;
    }

    public String getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String convertMessage() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return MessageConverter.getMessageConverter().convertMessage(getFrom(),getAuth(),getTo(),getMessage(),getAndSetTime());
    }

    public String getSendDateTimeAsString() {
        return sendDateTimeAsString;
    }

    public void setSendDateTimeAsString(String sendDateTimeAsString) {
        this.sendDateTimeAsString = sendDateTimeAsString;
    }
}
