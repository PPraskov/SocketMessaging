package client;

import client.constants.MessageConstants;
import client.exceptions.AuthorizationProblem;
import client.messages.MessageReceiving;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class InputMessageListener extends Thread {

    private User user;
    private Socket socket;
    private boolean toRun;
    private InputStreamReader reader;

    public InputMessageListener(User user) throws IOException {
        this.user = user;
        this.socket = this.user.getSocket();
        this.reader = new InputStreamReader(this.socket.getInputStream(),MessageConstants.ENCODING);
        this.toRun = true;
        Thread.yield();
    }


    @Override
    public void run() {
        while (toRun) {
            try {
                receiveAndAddMessages();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void waitForAuthentication() {
        try {
            while (!reader.ready()){
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            String[] message = receiveMessage();
            if (!message[0].equals(this.user.getName())){
                throw new AuthorizationProblem(AuthorizationProblem.WRONG_USERNAME_RECEIVED);
            }
            this.user.setAuth(message[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void receiveAndAddMessages() throws IOException {
        while (reader.ready()) {
            String[] stringArr = receiveMessage();
            MessageReceiving message = new MessageReceiving(stringArr[0], stringArr[1]);
            this.user.getInbox().addMessage(message);
        }
    }

    private String[] receiveMessage() throws IOException {
        int messageLength = readMessageLength(reader);
        String[] stringArr = new String[messageLength];
        for (int i = 0; i < messageLength; i++) {
            String received = readInputStream();
            stringArr[i] = received;
        }
        return stringArr;
    }

    private String readInputStream() throws IOException {
        int inputLength = readMessageLength(reader);
        return readInput(reader, inputLength);
    }

    private String readInput(InputStreamReader inputStream, int messageLength) throws IOException {
        char[] arr = new char[messageLength];
        inputStream.read(arr);
        return new String(arr);
    }

    private int readMessageLength(InputStreamReader inputStreamReader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        char c;
        while (inputStreamReader.ready()) {
            c = (char) inputStreamReader.read();
            if (Character.isDigit(c)) {
                stringBuilder.append(c);
            } else {
                if (c == ';') {
                    break;
                }
            }
        }
        if (stringBuilder.toString().isEmpty()){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            readMessageLength(inputStreamReader);
        }
        return Integer.parseInt(stringBuilder.toString());
    }

    public synchronized void stopRunning() {
        this.toRun = false;
        this.interrupt();
    }
}
