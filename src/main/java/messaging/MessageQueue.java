package messaging;

import messaging.util.MyCustomQueue;

import java.util.List;

class MessageQueue {

    private static final MyCustomQueue<Message> messageQueue = new MyCustomQueue<>();


    MessageQueue() {
    }

    void addMessage(Message message) {
        messageQueue.add(message);
        System.out.println("one added");
    }

    Message getMessage() {
        return messageQueue.poll();
    }

    void addAllMessages(List<Message> messages){
        messageQueue.addAll(messages);
    }
}
