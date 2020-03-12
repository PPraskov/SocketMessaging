package messaging.util;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class MyCustomQueue<T> {

    private volatile Queue<T> queue;
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
        synchronized (this.notEmpty) {
            while (this.queue.isEmpty()) {
                try {
                    this.notEmpty.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return this.queue.poll();
    }

    public void addAll(List<T> element) {
        synchronized (this.notEmpty) {
            this.queue.addAll(element);
            this.notEmpty.notifyAll();
        }
    }

}
