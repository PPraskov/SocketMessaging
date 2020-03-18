package messaging.persistence;

import messaging.authentication.User;
import messaging.messages.AbstractMessage;
import messaging.messages.AuthenticationMessage;
import messaging.messages.CommandMessage;
import messaging.messages.queue.InputQueue;
import messaging.messages.queue.OutputQueue;

import java.net.Socket;

public class PersistenceWorker extends Thread {
    private volatile boolean toRun;

    public PersistenceWorker() {
        this.setDaemon(true);
        this.setPriority(8);
        this.toRun = true;
    }

    @Override
    public void run() {
        InputQueue inputQueue = InputQueue.getQueue();
        OutputQueue outputQueue = OutputQueue.getQueue();
        PersistenceManager manager = PersistenceManager.getManager();
        while (toRun) {
            try {
                AbstractMessage message = inputQueue.getMessage();
                if (message instanceof AuthenticationMessage) {
                    User user = manager.saveUser(message.getSender());
                    message.setSender(user);
                } else if (message instanceof CommandMessage) {

                } else {

                }
                message.authenticateUser();
                if (message.getReceiver() == null) {
                    message.setReceiver(message.getSender());
                } else {
                    message = manager.persistMessage(message);
                }
                outputQueue.addMessage(message);

        } catch(Exception e){
            e.printStackTrace();
        }
    }

}

    public void stopRunning() {
        toRun = false;
    }
}
