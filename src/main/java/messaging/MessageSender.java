package messaging;

import messaging.maintenance.MemoryMonitor;
import messaging.messages.AbstractMessage;
import messaging.messages.MessageQueue;

class MessageSender extends Thread {

    private boolean toRun;
    private MemoryMonitor monitor;

    MessageSender(MemoryMonitor monitor){
        this.setDaemon(true);
        this.monitor = monitor;
        this.toRun = true;
    }

    @Override
    public void run() {
        MessageQueue queue = MessageQueue.getQueue();
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
