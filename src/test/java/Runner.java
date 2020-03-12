import messaging.MessagingManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Runner implements Runnable {

    private final Object object;
    private final Object[] args;

    public Runner(Object object, Object[] args) {
        this.object = object;
        this.args = args;
    }

    @Override
    public void run() {
        ((MessagingManager)this.object).initializeAndStart();
    }
}
