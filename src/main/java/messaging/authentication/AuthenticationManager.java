package messaging.authentication;

import java.net.Socket;
import java.util.Map;

public class AuthenticationManager implements ActiveUsersGetter {
    private static AuthenticationManager manager = null;
    private static volatile boolean alive;

    static {
        manager = new AuthenticationManager();
    }

    private AuthenticationManager() {
    }

    public static AuthenticationManager getManager() {
        AuthenticationManager m;
        if (!alive){
            createManager();
        }
        m = manager;
        return m;
    }

    private static void createManager() {
        manager = new AuthenticationManager();
    }

    public boolean authenticate(User user) {
        String authToken = TokenHolder.getTokenHolder().getActiveTokens().get(user.getUsername());
        String userToken = user.getToken();
        return authToken.equals(userToken);
    }

    public User generateTokenForNewUser(String username, Socket socket) {
        TokenHolder tokenHolder = TokenHolder.getTokenHolder();
        TokenGenerator generator = TokenGenerator.getGenerator();
        String token;
        do {
           token = generator.generateAuthToken();
        }while (tokenHolder.checkIfTokenIsPresent(token));
        tokenHolder.addToken(username,token);
        User user = new User(username, token, socket);
        ActiveUserHolder userHolder = ActiveUserHolder.getUserHolder();
        userHolder.addUser(user);
        return user;
    }

    public void addUser(User user) {
        ActiveUserHolder userHolder = ActiveUserHolder.getUserHolder();
        userHolder.addUser(user);
    }

    @Override
    public Map<String, User> getActiveUsers() {
        ActiveUserHolder userHolder = ActiveUserHolder.getUserHolder();
        return userHolder.getActiveUsers();
    }

    @Override
    public User getUser(String username) {
        ActiveUserHolder userHolder = ActiveUserHolder.getUserHolder();
        return userHolder.getUser(username);
    }

    @Override
    public boolean isUserActive(String username) {
       return ActiveUserHolder.getUserHolder().checkIfUserPresent(username);
    }
}
