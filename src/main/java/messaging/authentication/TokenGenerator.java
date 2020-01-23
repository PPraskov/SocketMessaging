package messaging.authentication;

import java.util.Random;

class TokenGenerator {

    private static final int TOKEN_LENGTH = 15;

    TokenGenerator() {
    }

    String generateAuthToken() {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        while (stringBuilder.length() < TOKEN_LENGTH) {
            int i = random.nextInt(74) + 48;
            if ((i > 57 && i < 74) || (i > 90 && i < 97)){
                continue;
            }
            char c = (char) i;
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }
}
