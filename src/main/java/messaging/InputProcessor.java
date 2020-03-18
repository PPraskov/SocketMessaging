package messaging;

import messaging.constants.MessageConstants;
import messaging.exception.UnrecognizedMessage;
import messaging.messages.AbstractMessage;
import messaging.messages.MessageFactory;
import messaging.messages.queue.InputQueue;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

class InputProcessor {
    private static InputProcessor inputProcessor = null;
    private static volatile boolean alive = false;

    private InputProcessor() {
    }

    public static InputProcessor getInputProcessor() {
        InputProcessor processor;
        if (!alive) {
            createInputProcessor();
        }
        processor = inputProcessor;
        return processor;
    }

    private static void createInputProcessor() {
        inputProcessor = new InputProcessor();
        alive = true;
    }


    void wrapToMessage(Socket socket) {
        try {
            readInputStream(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        try {
//                Map<Integer, String> map = convertToMessage(
//                        MessageConstants.MESSAGE_MESSAGE_LENGTH,
//                        socket.getInputStream());
//                User user = new User(
//                        map.get(MessageConstants.MESSAGE_FROM),
//                        map.get(MessageConstants.MESSAGE_AUTH),
//                        socket);
//                AuthenticationManager authenticationManager = AuthenticationManager.getManager();
//                if (!authenticationManager.authenticate(user)) {
//                    throw new UnauthorizedUser(user.getUsername());
//                }
//                if (!AuthenticationManager.getManager().isUserActive(map.get(MessageConstants.MESSAGE_TO))){
//                    OutputMessageWriter writer = OutputMessageWriter.getWriter();
//                    writer.flushMessage(user.getSocket().getOutputStream(),MessageConstants.USER_NOT_ACTIVE);
//                }
//                Message message = MessageFactory.getFactory().createMessage(
//                        user,
//                        map.get(MessageConstants.MESSAGE_TO),
//                        map.get(MessageConstants.MESSAGE_MESSAGE),
//                        map.get(MessageConstants.MESSAGE_DATETIME)
//                );
//                messages.add(message);
//            }
//        } catch (IOException | IllegalMessage | UnauthorizedUser e) {
//            e.printStackTrace();
//        }
//        return messages;
    }


    void readInputStream(Socket socket) throws IOException {
        InputStreamReader reader = new InputStreamReader(socket.getInputStream(), MessageConstants.ENCODING);
        int messageLength = readMessageLength(reader);
        String[] messageArr = new String[messageLength];
        for (int i = 0; i < messageLength; i++) {
            int inputLength = readMessageLength(reader);
            if (inputLength == 0) {
                throw new UnrecognizedMessage();
            }
            messageArr[i] = readInput(reader, inputLength);
        }
        AbstractMessage message;
        switch (messageLength) {
            case MessageConstants.MESSAGE_TYPE_AUTHENTICATION:
                message = MessageFactory.getFactory().createMessage(
                        messageArr[0],
                        messageArr[1],
                        messageArr[2],
                        messageArr[3],
                        socket);
                break;
            case MessageConstants.MESSAGE_TYPE_COMMAND:
                message = MessageFactory.getFactory().createMessage(
                        messageArr[0],
                        messageArr[1],
                        messageArr[2],
                        messageArr[3],
                        messageArr[4],
                        socket);
                break;
            case MessageConstants.MESSAGE_TYPE_CONTACT:
                message = MessageFactory.getFactory().createMessage(
                        messageArr[0],
                        messageArr[1],
                        messageArr[2],
                        messageArr[3],
                        messageArr[4],
                        messageArr[5],
                        socket);
                break;
            default:
                throw new UnrecognizedMessage();
        }
        InputQueue.getQueue().addMessage(message);
    }

    private String readInput(InputStreamReader inputStream, int messageLength) throws IOException {
        char[] chars = new char[messageLength];
        inputStream.read(chars);
        return new String(chars);
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
                } else {
                    throw new UnrecognizedMessage();
                }
            }
        }
        return Integer.parseInt(stringBuilder.toString());
    }
}
