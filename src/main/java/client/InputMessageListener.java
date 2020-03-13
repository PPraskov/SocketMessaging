package client;

import messaging.exception.UnrecognizedMessage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class InputMessageListener extends Thread {

    private User user;
    private Socket socket;
    private boolean toRun;

    public InputMessageListener(User user) {
        this.user = user;
        this.socket = this.user.getSocket();
        this.toRun = true;
    }


    @Override
    public void run() {
        while (toRun) {
            try {
                receiveMessages();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void waitForAuthentication() {
        try {
            String authentication = "";
            while (this.socket.getInputStream().available() == 0) {
            }
            String username = readInputStream(this.socket.getInputStream());
            authentication = readInputStream(this.socket.getInputStream());
            this.user.setAuth(authentication);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void receiveMessages() throws IOException {
        InputStream inputStream = this.user.getSocket().getInputStream();
        while (inputStream.available() > 0) {
            String[] stringArr = new String[2];
            for (int i = 0; i < stringArr.length; i++) {
                String received = readInputStream(inputStream);
                stringArr[i] = received;
            }
            this.user.getInbox().addMessage(new MessageReceiving(stringArr[0],stringArr[1]));
        }
    }

    private String readInputStream(InputStream inputStream) throws IOException {
        int inputLength = readMessageLength(inputStream);
        return readInput(inputStream, inputLength);
    }

    private String readInput(InputStream inputStream, int messageLength) throws IOException {
        byte[] arr = new byte[messageLength];
        inputStream.read(arr);
        return new String(arr);
    }

    private int readMessageLength(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        char c;
        while (inputStream.available() > 0) {
            c = (char) inputStream.read();
            if (Character.isDigit(c)) {
                stringBuilder.append(c);
            } else {
                if (c == ';') {
                    break;
                }
            }
        }
        return Integer.parseInt(stringBuilder.toString());
    }

    public synchronized void stopRunning() {
        this.toRun = false;
        this.interrupt();
    }
}
