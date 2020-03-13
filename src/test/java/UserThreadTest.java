import client.User;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class UserThreadTest extends Thread {

    private User user;
    private List<String> usernames;

    public UserThreadTest() {
        this.setPriority(2);
    }

    public User getUser() {
        return user;
    }

    @Override
    public void run() {
        initialize();
        startTalking(10);
    }

    private void startTalking(final int count) {
        int counter = 0;
        StringBuilder stringBuilder = new StringBuilder();
        String[] toArr = new String[count];
        String[] messageArr = new String[count];
        Random random = new Random();
        do {
            int length = random.nextInt(16) + 5;
            String message;
            stringBuilder.setLength(0);
            while (stringBuilder.length() < length) {
                int i = random.nextInt(74) + 48;
                if ((i > 57 && i < 74) || (i > 90 && i < 97)) {
                    continue;
                }
                char c = (char) i;
                stringBuilder.append(c);

            }
            message = stringBuilder.toString().trim();
            toArr[counter] = this.usernames.get(random.nextInt(this.usernames.size()));
            messageArr[counter] = message;
            counter++;
        } while (counter < count);
        try {
            this.user.sendMessages(toArr, messageArr);
            Thread.sleep(random.nextInt(100) + 50);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initialize() {
        UsernameGenerator usernameGenerator = new UsernameGenerator();
        this.user = new User(usernameGenerator.getUsername());
        this.usernames = usernameGenerator.getAll();
    }
}
