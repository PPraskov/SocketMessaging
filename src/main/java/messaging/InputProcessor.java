package messaging;

import messaging.authentication.AuthenticationManager;
import messaging.exception.UnauthorizedUser;
import messaging.exception.IllegalMessage;
import messaging.exception.UnrecognizedMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.*;

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


    List<Message> mapMessage(Socket socket) {
        List<Message> messages = new ArrayList<>();
        try {
            InputStream inputStream = socket.getInputStream();
            while (inputStream.available() > 0) {
                Map<Integer, byte[]> map = convertToMessage(
                        MessageConstants.MESSAGE_MESSAGE_LENGTH,
                        socket.getInputStream());
                User user = new User(
                        getPartFromMessage(MessageConstants.MESSAGE_FROM, map),
                        getPartFromMessage(MessageConstants.MESSAGE_AUTH, map),
                        socket);
                AuthenticationManager authenticationManager = AuthenticationManager.getManager();
                if (!authenticationManager.authenticate(user)) {
                    throw new UnauthorizedUser(user.getUsername());
                }
                Message message = MessageFactory.getFactory().createMessage(
                        user,
                        getPartFromMessage(MessageConstants.MESSAGE_TO, map),
                        map.get(MessageConstants.MESSAGE_MESSAGE));
                messages.add(message);
            }
        } catch (IOException | IllegalMessage | UnauthorizedUser e) {
            e.printStackTrace();
        }
        return messages;
    }

    String getPartFromMessage(int index, Map<Integer, byte[]> map) {
        return convertToString(map.get(index));
    }

    Map<Integer, byte[]> convertToMessage(int i, InputStream inputStream) {
        try {
            return readInputStream(i, inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String convertToString(byte[] arr) {
        return new String(arr);
    }

    Map<Integer, byte[]> readInputStream(int length, InputStream inputStream) throws IOException {
        Map<Integer, byte[]> map = new TreeMap<>();
        for (int i = 0; i < length; i++) {
            int inputLength = readMessageLength(inputStream);
            if (inputLength == 0) {
                throw new UnrecognizedMessage();
            }
            byte[] bytes = readInput(inputStream, inputLength);
            map.put(i, bytes);
        }
        return map;
    }

    private byte[] readInput(InputStream inputStream, int messageLength) throws IOException {
        byte[] bytes = new byte[messageLength];
        inputStream.read(bytes);
        return bytes;
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
                } else {
                    throw new UnrecognizedMessage();
                }
            }
        }
        return Integer.parseInt(stringBuilder.toString());
    }
}
