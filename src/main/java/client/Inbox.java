package client;

import java.util.ArrayList;
import java.util.List;

public class Inbox {
    private volatile List<MessageReceiving> messages;
    private final Object emptyCondition;
    private boolean isEmpty;

    public Inbox() {
        this.messages = new ArrayList<>();
        this.emptyCondition = new Object();
        this.isEmpty = true;
    }

    public void addMessage(MessageReceiving message) {
        synchronized (this.emptyCondition) {
            this.messages.add(message);
            this.isEmpty = false;
            this.emptyCondition.notify();
        }
    }
    public void addMessages(List<MessageReceiving> messages) {
        synchronized (this.emptyCondition) {
            this.messages.addAll(messages);
            this.isEmpty = false;
            this.emptyCondition.notifyAll();
        }

    }

    public MessageReceiving getMessage() {
        lockIfEmpty();
        return this.messages.get(0);
    }

    public List<MessageReceiving> getAllMessages(){
        lockIfEmpty();
        return this.messages;
    }

    private void lockIfEmpty(){
        if (isEmpty) {
            synchronized (this.emptyCondition) {
                if (this.messages.isEmpty()) {
                    this.isEmpty = true;
                    try {
                        this.emptyCondition.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
