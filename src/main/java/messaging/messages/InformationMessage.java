package messaging.messages;

import messaging.constants.MessageConstants;

import java.net.Socket;

public class InformationMessage extends AbstractMessage {

    public InformationMessage(String message, String dateTimeAsString, Socket socket) {
        super(null, null,null, message, dateTimeAsString, socket);
    }

    @Override
    public String convertMessage() {
        return MessageConverter.getMessageConverter()
                .convertMessage(MessageConstants.MESSAGE_SERVER, getMessage(), getDateTimeAsString());
    }
}
