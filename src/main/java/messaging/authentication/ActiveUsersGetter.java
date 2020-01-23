package messaging.authentication;

import messaging.User;

import java.util.Map;

public interface ActiveUsersGetter {

    Map<String, User> getActiveUsers();

    User getUser(String username);
}
