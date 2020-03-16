package messaging.messages;

import messaging.authentication.User;
import messaging.authentication.AuthenticationManager;
import messaging.exception.UnauthorizedUser;

import java.net.Socket;

public abstract class AbstractMessage {
    private User user;
    private final String receiver;
    private final String message;
    private final String dateTimeAsString;
    private final Socket socket;
    private final String username;
    private final String authenticationToken;

    AbstractMessage(String username, String authenticationToken, String receiver, String message, String dateTimeAsString, Socket socket) {
        this.username = username;
        this.authenticationToken = authenticationToken;
        this.receiver = receiver;
        this.message = message;
        this.dateTimeAsString = dateTimeAsString;
        this.socket = socket;
    }

    public void authenticateUser() {
        User user = null;
        AuthenticationManager manager = AuthenticationManager.getManager();
        if (this instanceof AuthenticationMessage) {
            user = manager.generateTokenForNewUser(username, socket);
            manager.addUser(user);
        } else if (this instanceof CommandMessage || this instanceof Message) {
            user = new User(username, authenticationToken, socket);
            boolean authenticate = manager.authenticate(user);
            if (!authenticate) {
                throw new UnauthorizedUser(username);
            }
        }
        setUser(user);
    }

    public String getReceiver() {
        return receiver;
    }

    public Socket getSocket() {
        return socket;
    }

    public User getUser() {
        return user;
    }

    void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public String getDateTimeAsString() {
        return dateTimeAsString;
    }

    public abstract String convertMessage();
}
