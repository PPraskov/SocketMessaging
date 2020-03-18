package messaging;

import messaging.maintenance.MemoryMonitor;
import messaging.messages.AbstractMessage;
import messaging.messages.queue.OutputQueue;

class MessageSender extends Thread {

    private boolean toRun;
    private MemoryMonitor monitor;

    MessageSender(MemoryMonitor monitor){
        this.setDaemon(true);
        this.setPriority(8);
        this.monitor = monitor;
        this.toRun = true;
    }

    @Override
    public void run() {
        OutputQueue queue = OutputQueue.getQueue();
        while (toRun){
            this.monitor.checkForLockLock();
            AbstractMessage message = queue.getMessage();
            OutputProcessor.getProcessor().processAndSendMessage(message);

        }
    }

    void stopRunning(){
        this.toRun = false;
    }
}
