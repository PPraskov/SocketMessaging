package messaging.authentication;

import java.net.Socket;

public class User {

    private final String username;
    private final String token;
    private final Socket socket;


    public User(String username, String token, Socket socket) {
        this.username = username;
        this.token = token;
        this.socket = socket;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public Socket getSocket() {
        return socket;
    }
}
