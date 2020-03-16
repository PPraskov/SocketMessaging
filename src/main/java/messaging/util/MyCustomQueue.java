package messaging.util;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MyCustomQueue<T> {

    private final BlockingQueue<T> queue;
    private final Lock lock;
    private final Condition empty;
    private volatile int added = 0;
    private volatile int taken = 0;

    public MyCustomQueue() {
        this.queue = new ArrayBlockingQueue<>(1000);
        this.lock = new ReentrantLock();
        empty = this.lock.newCondition();

    }

    public void add(T element) {
        try {
            lock.lock();
            queue.add(element);
            empty.signal();
        } finally {
            lock.unlock();
        }
    }

    public T poll() {
        try {
            lock.lock();
            while (queue.isEmpty()) {
                empty.awaitUninterruptibly();
            }
            return queue.poll();
        } finally {
            lock.unlock();
        }
    }

//    private boolean checkIfEmpty() {
//        try {
//            lock.lock();
//            boolean isEmpty = queue.isEmpty();
//            if (isEmpty) {
//                try {
//                    empty.await(100L, TimeUnit.MILLISECONDS);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            return isEmpty;
//        } finally {
//            lock.unlock();
//        }
//    }
}
