package messaging.authentication;

import java.util.Random;

class TokenGenerator {
    private static TokenGenerator generator;
    private static volatile boolean alive = false;
    private static final int TOKEN_LENGTH = 15;

    static {
        generator = new TokenGenerator();
    }
    private TokenGenerator() {
        alive = true;
    }

    public static TokenGenerator getGenerator() {
        TokenGenerator tokenGenerator;
        if (!alive){
            createTokenGenerator();
        }
        tokenGenerator = generator;
        return tokenGenerator;
    }

    private static void createTokenGenerator() {
        generator = new TokenGenerator();
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
