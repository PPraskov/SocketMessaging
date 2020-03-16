package messaging.messages;

import java.net.Socket;

public class AuthenticationMessage extends AbstractMessage {


    AuthenticationMessage(String username, String message, String dateTimeAsString, Socket socket) {
        super(username, null,username, message, dateTimeAsString,socket);
    }

    @Override
    public String convertMessage() {
        return MessageConverter.getMessageConverter().convertMessage(getUser().getUsername(),getUser().getToken(),getDateTimeAsString());
    }
}
