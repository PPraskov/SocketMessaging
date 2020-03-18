package client.messages;

import client.User;

import java.lang.reflect.InvocationTargetException;

public class MessageSending extends OutputMessage {


    public MessageSending(User user, String to, String message) {
        setFrom(user.getName());
        setPassword(user.getPassword());
        setAuth(user.getAuth());
        setTo(to);
        setMessage(message);
    }

    @Override
    public String convertMessage() {
        return MessageConverter.getMessageConverter().convertMessage(
                getFrom(),
                getPassword(),
                getAuth(),
                getTo(),
                getMessage(),
                getAndSetTime());
    }
}
