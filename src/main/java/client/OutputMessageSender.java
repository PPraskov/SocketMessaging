package client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

class OutputMessageSender {
    private User user;
    private Socket socket;

    OutputMessageSender(User user){
        this.user = user;
        this.socket = this.user.getSocket();
    }

     void sendAuthenticationRequest() {
        try {
            String toSend = String.format("from = %s\nauthenticateMe = please", this.user.getName());
            toSend = String.format("%d;%s", toSend.getBytes().length, toSend);
            OutputStream outputStream = this.socket.getOutputStream();
            outputStream.write(toSend.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



   void sendMessage(String sendTo, String message) throws IOException {
       MessageSending messageSending = new MessageSending(this.user, sendTo, message);
        String messageToStr = messageSending.toString();
        OutputStream outputStream = this.socket.getOutputStream();
        outputStream.write(messageToStr.getBytes());
        outputStream.flush();
        this.user.getSentMessages().addMessage(messageSending);
       try {
           Thread.sleep(50);
       } catch (InterruptedException e) {
           e.printStackTrace();
       }
   }
}
