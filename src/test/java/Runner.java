import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Runner implements Runnable {

    private final Object object;
    private final Method method;
    private final Object[] args;

    public Runner(Object object, Method method, Object[] args) {
        this.object = object;
        this.method = method;
        this.args = args;
    }

    @Override
    public void run() {
        try {
            this.method.invoke(this.object,this.args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
