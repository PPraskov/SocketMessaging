package client;

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
            authentication = readInputStream(this.socket.getInputStream());
            String[] authenticationArr = authentication.split("=");
            this.user.setAuth(authenticationArr[1].trim());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void receiveMessages() throws IOException {
        InputStream inputStream = this.user.getSocket().getInputStream();
        while (inputStream.available() > 0) {
            String received = readInputStream(inputStream);
            if (received.length() == 0) {
                return;
            }
            this.user.getInbox().addMessage(mapToMessage(received));
        }
    }

    private MessageReceiving mapToMessage(String received) {
        String[] strings = received.split("\n");
        String[] arg = new String[2];
        for (int i = 0; i < strings.length; i++) {
            String[] split = strings[i].split("=");
            arg[i] = split[1].trim();
        }
        return new MessageReceiving(arg[0], arg[1]);
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
        while (inputStream.available() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            char c;
            while ((c = (char) inputStream.read()) != ';') {
                stringBuilder.append(c);
            }
            String string = stringBuilder.toString();
            return Integer.parseInt(string);
        }
        return 0;
    }

    public synchronized void stopRunning() {
        this.toRun = false;
        this.interrupt();
    }
}
