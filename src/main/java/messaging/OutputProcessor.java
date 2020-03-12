package messaging;

import messaging.authentication.ActiveUsersGetter;
import messaging.authentication.AuthenticationManager;

import java.io.IOException;
import java.io.OutputStream;

class OutputProcessor{

    private final Message message;

    OutputProcessor(Message message) {
        this.message = message;
    }


    void sendMessage() {
        String receiverStr = message.getReceiver();
        ActiveUsersGetter usersGetter = new AuthenticationManager();
        User receiver = usersGetter.getUser(receiverStr);
        try {
            if (receiver == null){
                OutputStream outputStream = message.getSender().getSocket().getOutputStream();
                String message = "from = Messaging Server\nmessage = User not found!";
                message = String.format("%d;%s",message.getBytes().length,message);
                outputStream.write(message.getBytes());
                outputStream.flush();
            }
            else {
                OutputStream outputStream = receiver.getSocket().getOutputStream();
                String messageToString = convertMessageToString();
                outputStream.write(messageToString.getBytes());
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String convertMessageToString() {
        return message.convertToString();
    }


}
