import client.User;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class UserThreadTest extends Thread {

    private User user;
    private List<String> usernames;

    public UserThreadTest() {
        this.setPriority(5);
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
        do {
            Random random = new Random();
            int length = random.nextInt(16)+5;
            String message = null;
            stringBuilder.setLength(0);
            while (stringBuilder.length() < length) {
                int i = random.nextInt(74) + 48;
                if ((i > 57 && i < 74) || (i > 90 && i < 97)) {
                    continue;
                }
                char c = (char) i;
                stringBuilder.append(c);

            }
            try {
                message = stringBuilder.toString().trim();
                this.user.sendMessage(this.usernames.get(random.nextInt(this.usernames.size())), message);
                counter++;
                Thread.sleep(50);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } while (counter < count);
    }

    private void initialize() {
        UsernameGenerator usernameGenerator = new UsernameGenerator();
        this.user = new User(usernameGenerator.getUsername());
        this.usernames = usernameGenerator.getAll();
        List<String> collect = usernames.stream().filter(x -> !x.equals(this.user.getName())).collect(Collectors.toList());

    }
}
