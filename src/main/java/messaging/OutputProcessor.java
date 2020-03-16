package messaging;

import messaging.authentication.ActiveUsersGetter;
import messaging.authentication.AuthenticationManager;
import messaging.constants.MessageConstants;
import messaging.messages.AbstractMessage;
import messaging.messages.InformationMessage;

import java.io.IOException;
import java.net.Socket;

class OutputProcessor {
    private static OutputProcessor processor;
    private static volatile boolean alive = false;

    static {
        processor = new OutputProcessor();
        alive = true;
    }

    private OutputProcessor() {
    }

    public static OutputProcessor getProcessor() {
        OutputProcessor outputProcessor;
        if (!alive) {
            createProcessor();
        }
        outputProcessor = processor;
        return outputProcessor;
    }

    private static void createProcessor() {
        processor = new OutputProcessor();
        alive = true;
    }

    void sendMessage(AbstractMessage message) {
        try {
            OutputMessageWriter writer = OutputMessageWriter.getWriter();
            Socket sendTo;
            ActiveUsersGetter manager = AuthenticationManager.getManager();
            if (!manager.isUserActive(message.getReceiver())){
                sendTo = message.getUser().getSocket();
                message = new InformationMessage(MessageConstants.NO_USER_FOUND,message.getDateTimeAsString(),message.getSocket());
            }else {
                sendTo = manager.getUser(message.getReceiver()).getSocket();
            }
            writer.flushMessage(sendTo.getOutputStream(), message.convertMessage());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processAndSendMessage(AbstractMessage message) {
        message.authenticateUser();
        sendMessage(message);
    }
}
