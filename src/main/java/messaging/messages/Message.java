package messaging.messages;

import messaging.authentication.User;

import java.net.Socket;

class Message extends AbstractMessage {

    Message(String username, String authenticationToken, String receiver, String message, String dateTimeAsString, Socket socket) {
        super(username,authenticationToken,receiver, message, dateTimeAsString,socket);

    }

    public String getMessage() {
        return super.getMessage();
    }

    User getSender() {
        return super.getUser();
    }

    @Override
    public String convertMessage() {
        return MessageConverter.getMessageConverter().convertMessage(getSender().getUsername(), getMessage(), getDateTimeAsString());
    }
}

