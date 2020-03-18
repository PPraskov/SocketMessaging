package messaging.messages;

import java.net.Socket;

class ContactMessage extends AbstractMessage {

    ContactMessage(String username,String password, String authenticationToken, String receiver, String message, String dateTimeAsString, Socket socket) {
        super(username,password,authenticationToken,receiver, message, dateTimeAsString,socket);

    }
    ContactMessage(){

    }

    public String getMessage() {
        return super.getMessage();
    }


    @Override
    public String convertMessage() {
        return MessageConverter.getMessageConverter().convertMessage(
                getId(),
                getSender().getUsername(),
                getMessage(),
                getDateTimeAsString());
    }
}

