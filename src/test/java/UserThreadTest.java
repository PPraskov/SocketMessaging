import client.User;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class UserThreadTest extends Thread {

    private User user;
    private List<String> usernames;

    public UserThreadTest() {
        this.setPriority(7);
    }

    public User getUser() {
        return user;
    }

    @Override
    public void run() {
        try {
            initialize();
            startTalking(10);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startTalking(final int count) {
        int counter = 0;
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        String to;
        String message;
        do {
            int length = random.nextInt(16) + 5;
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
            to = this.usernames.get(random.nextInt(this.usernames.size()));

            try {
                this.user.sendMessage(to, message);
                counter++;
            } catch (IOException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } while (counter < count);
    }

    private void initialize() throws IOException {
        UsernameGenerator usernameGenerator = new UsernameGenerator();
        this.user = new User(usernameGenerator.getUsername());
        this.usernames = usernameGenerator.getAll();
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
