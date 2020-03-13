package messaging;

class MessageSender extends Thread {

    private boolean toRun;
    private MemoryMonitor monitor;

    MessageSender(MemoryMonitor monitor){
        this.setDaemon(true);
        this.setPriority(9);
        this.monitor = monitor;
        this.toRun = true;
    }

    @Override
    public void run() {
        MessageQueue queue = MessageQueue.getQueue();
        while (toRun){
            this.monitor.checkForLockLock();
            Message message = queue.getMessage();
            OutputProcessor.getProcessor().sendMessage(message);

        }
    }

    void stopRunning(){
        this.toRun = false;
    }
}
