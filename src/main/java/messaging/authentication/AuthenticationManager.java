package messaging.authentication;

import messaging.User;

import java.net.Socket;
import java.util.Map;

public class AuthenticationManager implements ActiveUsersGetter {

    public AuthenticationManager() {
    }

    public boolean authenticate(User user) {
        String authToken = new TokenHolder().getActiveTokens().get(user.getUsername());
        String userToken = user.getToken();
        return authToken.equals(userToken);
    }

    public User generateTokenForNewUser(String username, Socket socket) {
        TokenHolder tokenHolder = new TokenHolder();
        TokenGenerator generator = new TokenGenerator();
        String token;
        do {
           token = generator.generateAuthToken();
        }while (tokenHolder.checkIfTokenIsPresent(token));
        tokenHolder.addToken(username,token);
        User user = new User(username, token, socket);
        ActiveUserHolder userHolder = new ActiveUserHolder();
        userHolder.addUser(user);
        return user;
    }

    public void addUser(User user) {
        ActiveUserHolder userHolder = new ActiveUserHolder();
        userHolder.addUser(user);
    }

    @Override
    public Map<String, User> getActiveUsers() {
        ActiveUserHolder userHolder = new ActiveUserHolder();
        return userHolder.getActiveUsers();
    }

    @Override
    public User getUser(String username) {
        ActiveUserHolder userHolder = new ActiveUserHolder();
        return userHolder.getUser(username);
    }
}
