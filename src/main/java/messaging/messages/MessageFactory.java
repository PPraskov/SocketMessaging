package messaging.messages;

import java.net.Socket;

public class MessageFactory {
    private static MessageFactory factory;
    private static volatile boolean alive = false;

    private MessageFactory() {

    }

    public static MessageFactory getFactory() {
        MessageFactory messageFactory;
        if (!alive) {
            createFactory();
        }
        messageFactory = factory;
        return messageFactory;
    }

    private static void createFactory() {
        factory = new MessageFactory();
        alive = true;
    }

    public AbstractMessage createMessage(String username,String password, String authenticationToken, String to, String messageStr, String dateTimeAsString, Socket socket) {
        return new ContactMessage(username,password, authenticationToken, to, messageStr, dateTimeAsString, socket);
    }

    public AbstractMessage createMessage(String username,String password, String authenticationToken, String command, String dateTimeAsString, Socket socket) {
        return new CommandMessage(username,password, authenticationToken, command, dateTimeAsString, socket);
    }

    public AbstractMessage createMessage(String username,String password, String command, String dateTimeAsString, Socket socket) {
        return new AuthenticationMessage(username,password, command, dateTimeAsString, socket);
    }
}
