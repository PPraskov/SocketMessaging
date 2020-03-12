package messaging.util;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class MyCustomQueue<T> {

    private volatile Queue<T> queue;
    private final Object notEmpty;
    private volatile int size;

    public MyCustomQueue() {
        this.queue = new ArrayDeque<>();
        this.notEmpty = new Object();
        this.size = 0;
    }

    public void add(T element) {
        synchronized (this.notEmpty) {
            this.queue.add(element);
            size++;

            this.notEmpty.notify();
        }
    }

    public T poll() {
        T result;
        if (size == 0) {
            synchronized (this.notEmpty) {
                try {
                    this.notEmpty.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        int temp = size - 1;
        result = this.queue.poll();
        size = temp;
        return result;
    }

    public void addAll(List<T> messages) {
        for (T message : messages) {
            add(message);
        }
    }
}
