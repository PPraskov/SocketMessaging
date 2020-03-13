package messaging;

import messaging.authentication.ActiveUsersGetter;
import messaging.authentication.AuthenticationManager;

import java.io.IOException;
import java.io.OutputStream;

class OutputProcessor{
    private static OutputProcessor processor;
    private static volatile boolean alive = false;

    private static String NO_USER_FOUND = "from = Messaging Server\nmessage = User not found!";

    static {
        processor = new OutputProcessor();
        alive = true;
    }

    private OutputProcessor() {}

    public static OutputProcessor getProcessor() {
        OutputProcessor outputProcessor;
        if (!alive){
            createProcessor();
        }
        outputProcessor = processor;
        return outputProcessor;
    }

    private static void createProcessor() {
        processor = new OutputProcessor();
        alive = true;
    }

    void sendMessage(Message message) {
        String receiverStr = message.getReceiver();
        ActiveUsersGetter usersGetter = AuthenticationManager.getManager();
        User receiver = usersGetter.getUser(receiverStr);
        try {
            OutputMessageWriter writer = OutputMessageWriter.getWriter();
            OutputStream outputStream;
            String messageStr;
            if (receiver == null){
                outputStream = message.getSender().getSocket().getOutputStream();
                messageStr = String.format("%d;%s",NO_USER_FOUND.getBytes().length,NO_USER_FOUND);
                writer.flushMessage(outputStream,messageStr);
            }
            else {
                outputStream = receiver.getSocket().getOutputStream();
                writer.flushMessage(outputStream,message.convertToByteArr());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
