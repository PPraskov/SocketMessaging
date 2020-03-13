package messaging;

import messaging.util.MyCustomQueue;

import java.util.List;

class MessageQueue {
    private static MessageQueue queue;
    private static volatile boolean alive = false;
    private final MyCustomQueue<Message> messageQueue;

    static {
        queue = new MessageQueue();
        alive = true;
    }

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

    void addMessage(Message message) {
        messageQueue.add(message);
        System.out.println("one added");
    }

    Message getMessage() {
        return messageQueue.poll();
    }

    void addAllMessages(List<Message> messages) {
        messageQueue.addAll(messages);
    }
}
