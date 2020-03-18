package messaging.authentication;

import java.net.Socket;

public class User {
    private String id;
    private String username;
    private String password;
    private String token;
    private Socket socket;


    public User(String username,String password, String token, Socket socket) {
        this.username = username;
        this.password = password;
        this.token = token;
        this.socket = socket;
    }

    User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
