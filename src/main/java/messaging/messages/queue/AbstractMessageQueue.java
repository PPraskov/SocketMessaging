package messaging.messages.queue;

import messaging.messages.AbstractMessage;

abstract class AbstractMessageQueue {

    private final MyCustomQueue<AbstractMessage> messageQueue;

    AbstractMessageQueue(){
        messageQueue = new MyCustomQueue<>();
    }

    public AbstractMessage getMessage() {
        return messageQueue.poll();
    }

    public void addMessage(AbstractMessage message) {
        messageQueue.add(message);
    }

}
