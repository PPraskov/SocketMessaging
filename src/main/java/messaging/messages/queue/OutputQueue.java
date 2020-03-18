package messaging.messages.queue;

public class OutputQueue extends AbstractMessageQueue {
    private static OutputQueue queue;
    private static volatile boolean alive;

    OutputQueue(){

    }

    public static OutputQueue getQueue() {
        OutputQueue messageQueue;
        if (!alive) {
            createMessageQueue();
        }
        messageQueue = queue;
        return messageQueue;
    }
    private static void createMessageQueue() {
        queue = new OutputQueue();
        alive = true;
    }
}
