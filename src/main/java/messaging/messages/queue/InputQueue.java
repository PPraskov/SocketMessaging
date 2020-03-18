package messaging.messages.queue;

public class InputQueue extends AbstractMessageQueue {
    private static InputQueue queue;
    private static volatile boolean alive;

    private InputQueue(){
        super();
    }

    public static InputQueue getQueue() {
        InputQueue messageQueue;
        if (!alive) {
            createMessageQueue();
        }
        messageQueue = queue;
        return messageQueue;
    }
    private static void createMessageQueue() {
        queue = new InputQueue();
        alive = true;
    }
}
