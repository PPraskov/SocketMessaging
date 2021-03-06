import java.util.*;
import java.util.stream.Collectors;

public class UsernameGenerator {

    private static int counter;
    private static final List<String> usernames;
    private static final Object lock;
    private static int maxNames;
    private static boolean ready;

    static {
        usernames = new ArrayList<>();
        counter = 0;
        lock = new Object();
        ready = false;
    }

    public String getUsername() {
        String result;
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        while (stringBuilder.length() < 10) {
            int i = random.nextInt(74) + 48;
            if ((i > 57 && i < 74) || (i > 90 && i < 97)) {
                continue;
            }
            char c = (char) i;
            stringBuilder.append(c);
        }
        result = stringBuilder.toString().trim();
        boolean rec = false;
        synchronized (usernames) {
            if (!usernames.contains(result)) {
                usernames.add(result);
                counter++;
                synchronized (lock) {
                    if (counter >= maxNames) {
                        ready = true;
                        lock.notifyAll();
                    }
                }
            } else {
                rec = true;
            }
        }
        if (rec)
            result = getUsername();
        return result;
    }

    public List<String> getAll() {
        if (!ready) {
            synchronized (lock) {
                try {
                    lock.wait(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return usernames;
    }

    public void setMax(int max) {
        maxNames = max;
    }
}
