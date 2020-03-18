package messaging.messages;

import java.net.Socket;

public class AuthenticationMessage extends AbstractMessage {


    AuthenticationMessage(String username,String password, String message, String dateTimeAsString, Socket socket) {
        super(username,password, null,null, message, dateTimeAsString,socket);
    }

    @Override
    public String convertMessage() {
        return MessageConverter.getMessageConverter().convertMessage(
                getSender().getId(),
                getSender().getUsername(),
                getSender().getToken(),
                getDateTimeAsString());
    }
}
