package client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class User {

    private final String name;
    private final Object authCondition;
    private final Inbox inbox;
    private final InputMessageListener messageListener;
    private final OutputMessageSender messageSender;
    private volatile String auth;
    private Socket socket;

    public User(String name) {
        this.name = name;
        try {
            this.socket = new Socket("localhost", 3600);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.authCondition = new Object();
        this.inbox = new Inbox();
        this.messageListener = new InputMessageListener(this);
        messageSender = new OutputMessageSender(this);
        authenticateUser();
        this.messageListener.start();
    }

    private void authenticateUser() {
        this.messageSender.sendAuthenticationRequest();
        this.messageListener.waitForAuthentication();
    }


    public String getName() {
        return name;
    }

    String getAuth() {
        return auth;

    }

    void setAuth(String auth) {
        synchronized (authCondition) {
            this.auth = auth;
            authCondition.notify();
        }
    }

    public Object getAuthCondition() {
        return authCondition;
    }

    Socket getSocket() {
        return socket;
    }

    public Inbox getInbox() {
        return inbox;
    }

    public void sendMessage(String sendTo, String message) throws IOException {
        if (auth == null) {
            synchronized (authCondition) {
                try {
                    authCondition.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        this.messageSender.sendMessage(sendTo, message);
    }

    public void stopListening() {
        this.messageListener.stopRunning();
    }

    Object authCondition(){
        return authCondition;
    }
}
