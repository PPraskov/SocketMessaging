package messaging.authentication;

import java.util.Map;

public interface ActiveUsersGetter {

    Map<String, User> getActiveUsers();

    User getUser(String username);

    boolean isUserActive(String username);
}
