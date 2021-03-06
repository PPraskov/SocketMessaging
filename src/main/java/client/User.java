package client;

import client.constants.ServerConstants;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

public class User {
    private String id;
    private final String name;
    private final String password;
    private final Object authCondition;
    private final Inbox inbox;
    private final SentMessages sentMessages;
    private final InputMessageListener messageListener;
    private final OutputMessageSender messageSender;
    private String auth;
    private Socket socket;

    public User(String name,String password) throws IOException {
        this.name = name;
        this.password = password;
        try {
            this.socket = new Socket(ServerConstants.ADDRESS, ServerConstants.PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.authCondition = new Object();
        this.inbox = new Inbox();
        this.messageListener = new InputMessageListener(this);
        messageSender = new OutputMessageSender(this);
        authenticateUser();
        this.messageListener.start();
        sentMessages = new SentMessages();
    }

    private void authenticateUser() {
        this.messageSender.sendAuthenticationRequest();
        this.messageListener.waitForAuthentication();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getAuth() {
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

    public SentMessages getSentMessages() {
        return sentMessages;
    }

    public void sendMessage(String sendTo, String message) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
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

    Object authCondition() {
        return authCondition;
    }

    public void sendMessages(String[] toArr, String[] messageArr) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        for (int i = 0; i < toArr.length; i++) {
            sendMessage(toArr[i], messageArr[i]);
        }
    }
}
