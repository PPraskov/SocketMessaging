package messaging;

import messaging.authentication.AuthenticationManager;
import messaging.exception.IllegalMessage;
import messaging.exception.UnrecognizedMessage;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

class InitialAuthentication implements InitialSocketAuthentication {
    private static String NO_SENDER_MESSAGE = "No username provided,socket will be closed!";
    private static String REQUEST_AUTHENTICATION_MESSAGE = "reqAuth";
    private static final int MESSAGE_LENGTH = 2;
    private static final int FROM = 0;
    private static final int AUTH_INDEX = 1;

    @Override
    public void authenticateSocket(Socket socket) throws IOException {
        while (socket.getInputStream().available() == 0) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        InputProcessor processor = InputProcessor.getInputProcessor();
        Map<Integer, byte[]> map = processor.convertToMessage(MESSAGE_LENGTH, socket.getInputStream());
        OutputMessageWriter writer = OutputMessageWriter.getWriter();

        String sender = processor.getPartFromMessage(FROM, map);
        if (processor.getPartFromMessage(AUTH_INDEX, map).equals(REQUEST_AUTHENTICATION_MESSAGE)) {
            AuthenticationManager authenticationManager = AuthenticationManager.getManager();
            User u = authenticationManager.generateTokenForNewUser(sender, socket);
            authenticationManager.addUser(u);
            int usernameLength = u.getUsername().getBytes().length;
            int tokenLength = u.getToken().getBytes().length;
            String token = String.format("%d;%s%d;%s", usernameLength, u.getUsername(), tokenLength, u.getToken());
            writer.flushMessage(socket.getOutputStream(), token);
        } else {
            throw new UnrecognizedMessage();
        }
    }
}