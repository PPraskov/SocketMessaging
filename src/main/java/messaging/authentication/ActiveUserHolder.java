package messaging.authentication;

import messaging.User;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class ActiveUserHolder {
    private static ActiveUserHolder userHolder;
    private static volatile boolean alive = false;
    private static ConcurrentMap<String, User> activeUsers;

    static {
        userHolder = new ActiveUserHolder();
        alive = true;
    }

    private ActiveUserHolder() {
        activeUsers = new ConcurrentHashMap<>();
    }

    public static ActiveUserHolder getUserHolder() {
        ActiveUserHolder holder;
        if (!alive){
            createUserHolder();
        }
        holder = userHolder;
        return holder;
    }

    private static void createUserHolder() {
        userHolder = new ActiveUserHolder();
    }

    void addUser(User user) {
        if (!checkIfUserPresent(user.getUsername())) {
            activeUsers.put(user.getUsername(), user);
        }
    }

    void removeUser(User user) {
        if (checkIfUserPresent(user.getUsername())) {
            activeUsers.remove(user);
            try {
                user.getSocket().close();
            } catch (IOException e) {
                System.out.println("Removing user went wrong!");
                e.printStackTrace();
            }
        }
    }

    boolean checkIfUserPresent(String username) {
        return activeUsers.containsKey(username);
    }

    Map<String, User> getActiveUsers() {
        return Collections.unmodifiableMap(new HashMap<>(activeUsers));
    }

    User getUser(String username) {
        if (checkIfUserPresent(username)) {
            return activeUsers.get(username);
        }
        return null;
    }
}
