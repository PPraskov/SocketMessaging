package messaging;

import messaging.util.MyCustomQueue;

class MessageQueue {

    private static final MyCustomQueue<Message> messageQueue = new MyCustomQueue<>();


    MessageQueue() {
    }

    void addMessage(Message message) {
        messageQueue.add(message);
    }

    Message getMessage() {
        return messageQueue.poll();
    }
}
