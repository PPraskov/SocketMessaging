package messaging;

import messaging.authentication.AuthenticationManager;
import messaging.authentication.User;
import messaging.constants.MessageConstants;
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
        processor.wrapToMessage(socket);
//        OutputMessageWriter writer = OutputMessageWriter.getWriter();
//
//        String sender = map.get(FROM);
//        String auth = map.get(AUTH_INDEX);
//        if (REQUEST_AUTHENTICATION_MESSAGE.equals(auth)) {
//            AuthenticationManager authenticationManager = AuthenticationManager.getManager();
//            User u = authenticationManager.generateTokenForNewUser(sender, socket);
//            authenticationManager.addUser(u);
//            int usernameLength = u.getUsername().getBytes().length;
//            int tokenLength = u.getToken().getBytes().length;
//            String token = String.format(MessageConstants.OUTPUT_MESSAGE_PATTERN_PART +
//                    MessageConstants.OUTPUT_MESSAGE_PATTERN_PART,
//                    usernameLength,
//                    u.getUsername(),
//                    tokenLength,
//                    u.getToken());
//            writer.flushMessage(socket.getOutputStream(), token);
//        } else {
//            throw new UnrecognizedMessage();
//        }
    }
}