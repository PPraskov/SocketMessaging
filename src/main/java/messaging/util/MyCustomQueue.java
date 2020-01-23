package messaging.util;

import java.util.ArrayDeque;
import java.util.Queue;

public class MyCustomQueue<T> {

    private final Queue<T> queue;
    private final Object notEmpty;

    public MyCustomQueue() {
        this.queue = new ArrayDeque<>();
        this.notEmpty = new Object();
    }

    public void add(T element) {
        synchronized (this.notEmpty) {
            this.queue.add(element);
            this.notEmpty.notify();
        }
    }

    public T poll() {
        while (this.queue.isEmpty()){
            synchronized (this.notEmpty) {
                try {
                    notEmpty.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return this.queue.poll();
    }


}
