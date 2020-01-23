package messaging.authentication;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class TokenHolder {

    private static final ConcurrentHashMap<String, String> tokens = new ConcurrentHashMap<>();

    TokenHolder() {
    }


    Map<String, String> getActiveTokens() {
        return Collections.unmodifiableMap(new HashMap<>(tokens));
    }

    void addToken(String username, String token) {
        tokens.put(username, token);
    }

    void removeToken(String username) {
        tokens.remove(username);
    }

    boolean checkIfTokenIsPresent(String token) {
        boolean present = false;
        for (String t : tokens.values()
        ) {
            if (t.equals(token)) {
                present = true;
                break;
            }
        }
        return present;
    }
}
