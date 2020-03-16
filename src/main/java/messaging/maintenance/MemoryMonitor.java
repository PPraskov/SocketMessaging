package messaging.maintenance;

public class MemoryMonitor extends Thread {

    private long heapMaxMemory;
    private final Object lock;
    private boolean lockFlag;

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
            if (checkMemory()) {
                lock();
            } else {
                unlock();
            }
        }
    }

    private boolean checkMemory() {
        boolean lockOn = false;
        if (((this.heapMaxMemory - getCurrentMemory()) / this.heapMaxMemory) > 0.85d) {
            System.gc();
            lockOn = true;
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return lockOn;
    }

    private long getCurrentMemory() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    public Object checkForLockLock() {
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
