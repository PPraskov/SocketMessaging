package client;

import client.messages.AuthenticationMessage;
import client.messages.MessageConverter;
import client.messages.MessageSending;
import client.messages.OutputMessage;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

class OutputMessageSender {
    private User user;
    private Socket socket;

    OutputMessageSender(User user) {
        this.user = user;
        this.socket = this.user.getSocket();
    }

    void sendAuthenticationRequest() {
        try {
            sendMessage((new AuthenticationMessage(this.user).convertMessage()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(char[] message) throws IOException {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.socket.getOutputStream(), StandardCharsets.UTF_8);
        outputStreamWriter.write(message);
        outputStreamWriter.flush();
        waitABit();
    }

    private void sendMessage(String message) throws IOException {
        sendMessage(message.toCharArray());
    }

    void sendMessage(String sendTo, String message) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        OutputMessage messageSending = new MessageSending(this.user, sendTo, message);
        sendMessage(messageSending.convertMessage());
        this.user.getSentMessages().addMessage(messageSending);
        waitABit();
    }

    private void waitABit() {
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
