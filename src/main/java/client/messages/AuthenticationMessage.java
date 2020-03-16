package client.messages;

import client.User;
import client.constants.MessageConstants;

public class AuthenticationMessage extends OutputMessage {


   public AuthenticationMessage(User user) {
        setFrom(user.getName());
    }

    @Override
    public String convertMessage() {
        return MessageConverter.getMessageConverter().convertMessage(getFrom(), MessageConstants.AUTHENTICATION_REQUEST_MESSAGE,getAndSetTime());
    }
}
