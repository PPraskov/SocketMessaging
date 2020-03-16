package messaging.messages;

import messaging.util.MyCustomQueue;

public class MessageQueue {
    private static MessageQueue queue;
    private static volatile boolean alive = false;
    private final MyCustomQueue<AbstractMessage> messageQueue;


    public static MessageQueue getQueue() {
        MessageQueue messageQueue;
        if (!alive) {
            createMessageQueue();
        }
        messageQueue = queue;
        return messageQueue;
    }

    private static void createMessageQueue() {
        queue = new MessageQueue();
        alive = true;
    }

    private MessageQueue() {
        messageQueue = new MyCustomQueue<>();
    }


    public AbstractMessage getMessage() {
        return messageQueue.poll();
    }

    public void addMessage(AbstractMessage message) {
        messageQueue.add(message);
    }

}
