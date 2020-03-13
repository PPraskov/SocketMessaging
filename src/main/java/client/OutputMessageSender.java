package client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

class OutputMessageSender {
    private User user;
    private Socket socket;

    OutputMessageSender(User user) {
        this.user = user;
        this.socket = this.user.getSocket();
    }

    void sendAuthenticationRequest() {
        try {
            AuthenticationMessage authenticationMessage = new AuthenticationMessage(this.user);
            sendMessageAsByteArr(authenticationMessage.convertToByteArr());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessageAsByteArr(byte[] arr) throws IOException {
        OutputStream outputStream = this.socket.getOutputStream();
        outputStream.write(arr);
        outputStream.flush();
        waitABit();
    }


    void sendMessageAsByteArr(String sendTo, String message) throws IOException {
        MessageSending messageSending = new MessageSending(this.user, sendTo, message);
        sendMessageAsByteArr(messageSending.convertToByteArr());
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
