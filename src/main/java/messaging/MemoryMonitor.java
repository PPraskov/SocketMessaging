package messaging;

public class MemoryMonitor extends Thread {

    private long heapMaxMemory;
    private final Object lock;
    private volatile boolean lockFlag;

    public MemoryMonitor() {
        this.setDaemon(true);
        this.setPriority(10);
        this.heapMaxMemory = Runtime.getRuntime().maxMemory();
        lock = new Object();
        lockFlag = false;
    }

    @Override
    public void run() {
        while (true) {
            while (((this.heapMaxMemory - getCurrentMemory()) / this.heapMaxMemory) > 0.85) {
                lock();
                System.gc();
            }
            unlock();
        }
    }

    private long getCurrentMemory() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    Object checkForLockLock() {
        if (lockFlag) {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return lock;
    }

    private void lock() {
        lockFlag = true;
    }

    private void unlock() {
        synchronized (lock) {
            lockFlag = false;
            lock.notifyAll();
        }
    }
}
