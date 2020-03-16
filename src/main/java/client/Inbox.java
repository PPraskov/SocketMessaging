package client;

import client.messages.InputMessage;

import java.util.ArrayList;
import java.util.List;

public class Inbox {
    private volatile List<InputMessage> messages;
    private final Object emptyCondition;
    private boolean isEmpty;

    public Inbox() {
        this.messages = new ArrayList<>();
        this.emptyCondition = new Object();
        this.isEmpty = true;
    }

    public void addMessage(InputMessage message) {
        synchronized (this.emptyCondition) {
            this.messages.add(message);
            this.isEmpty = false;
            this.emptyCondition.notifyAll();
        }
    }
    public void addMessages(List<InputMessage> messages) {
        synchronized (this.emptyCondition) {
            this.messages.addAll(messages);
            this.isEmpty = false;
            this.emptyCondition.notifyAll();
        }

    }

    public InputMessage getMessage() {
        lockIfEmpty();
        return this.messages.get(0);
    }

    public List<InputMessage> getAllMessages(){
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
