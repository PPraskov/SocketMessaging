package messaging.messages;

import messaging.authentication.User;
import messaging.authentication.AuthenticationManager;
import messaging.exception.UnauthorizedUser;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

public abstract class AbstractMessage {
    private User sender;
    private User receiver;
    private String id;
    private String message;
    private String dateTimeAsString;
    private String authenticationToken;

    AbstractMessage(String username,String password, String authenticationToken, String receiver, String message, String dateTimeAsString, Socket socket) {
        this.sender = generateUser(username);
        this.sender.setSocket(socket);
        this.sender.setPassword(password);
        this.authenticationToken = authenticationToken;
        this.receiver = receiver == null ? null: generateUser(receiver);
        this.message = message;
        this.dateTimeAsString = dateTimeAsString;
    }

    AbstractMessage(){
    }

    private User generateUser(String username) {
        User user = null;
        try {
            user = constructUser();
            user.setUsername(username);
            return user;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void authenticateUser() {
        User user = this.sender;
        AuthenticationManager manager = AuthenticationManager.getManager();
        if (this instanceof AuthenticationMessage) {
            user = manager.generateTokenForNewUser(user.getId(),user.getUsername(),user.getPassword(), user.getSocket());
            manager.addUser(user);
            this.sender = user;
        } else if (this instanceof CommandMessage || this instanceof ContactMessage) {
            user = new User(user.getUsername(),user.getPassword(), authenticationToken, user.getSocket());
            boolean authenticate = manager.authenticate(user);
            if (!authenticate) {
                throw new UnauthorizedUser(user.getUsername());
            }
        }
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateTimeAsString() {
        return dateTimeAsString;
    }

    public void setDateTimeAsString(String dateTimeAsString) {
        this.dateTimeAsString = dateTimeAsString;
    }

    public abstract String convertMessage();

    private User constructUser() throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        Constructor<User> constructor = User.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        User user = constructor.newInstance();
        constructor.setAccessible(false);
        return user;
    }

}
