package client.messages;

import client.User;
import client.constants.MessageConstants;

public class AuthenticationMessage extends OutputMessage {


   public AuthenticationMessage(User user) {
        setFrom(user.getName());
        setPassword(user.getPassword());
    }

    @Override
    public String convertMessage() {
        return MessageConverter.getMessageConverter().convertMessage(
                getFrom(),
                getPassword(),
                MessageConstants.AUTHENTICATION_REQUEST_MESSAGE,
                getAndSetTime());
    }
}
