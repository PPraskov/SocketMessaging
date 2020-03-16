import messaging.MessagingManager;

public class Runner implements Runnable {

    private final Object object;

    public Runner(Object object) {
        this.object = object;
    }

    @Override
    public void run() {
        ((MessagingManager)this.object).initializeAndStart();
    }
}
