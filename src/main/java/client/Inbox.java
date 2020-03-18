package client;

import client.messages.InputMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Inbox {
    private volatile BlockingQueue<InputMessage> messages;
//    private final Object emptyCondition;
//    private boolean isEmpty;

    public Inbox() {
        this.messages = new ArrayBlockingQueue<>(10000);
//        this.emptyCondition = new Object();
//        this.isEmpty = true;
    }

    public void addMessage(InputMessage message) {
//        synchronized (this.emptyCondition) {
            this.messages.add(message);
//            this.isEmpty = false;
//            this.emptyCondition.notifyAll();
//        }
    }
    public void addMessages(List<InputMessage> messages) {
        for (InputMessage m: messages
             ) {
            addMessage(m);
        }
    }

    public InputMessage getMessage() {
        InputMessage message = null;
        do {
            try {
                message = messages.poll(100L, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }while (message == null);
        return message;
    }

    public List<InputMessage> getAllMessages(){
        List<InputMessage> inputMessages = new ArrayList<>();
       messages.drainTo(inputMessages);
       return inputMessages;
    }

//    private void lockIfEmpty(){
//        if (isEmpty) {
//            synchronized (this.emptyCondition) {
//                if (this.messages.isEmpty()) {
//                    this.isEmpty = true;
//                    try {
//                        this.emptyCondition.wait();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }
}
