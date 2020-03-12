package messaging;

class MessageSender extends Thread {

    private boolean toRun;
    private MemoryMonitor monitor;

    MessageSender(MemoryMonitor monitor){
        this.monitor = monitor;
        this.toRun = true;
    }

    @Override
    public void run() {
        MessageQueue queue = new MessageQueue();
        while (toRun){
            this.monitor.checkForLockLock();
            Message message = queue.getMessage();
            OutputProcessor processor = new OutputProcessor(message);
            processor.sendMessage();
        }
    }

    void stopRunning(){
        this.toRun = false;
    }
}
