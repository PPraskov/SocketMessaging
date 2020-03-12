package messaging;

import messaging.authentication.AuthenticationManager;
import messaging.exception.IllegalAuthorization;
import messaging.exception.IllegalMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class InputProcessor {

    InputProcessor() {
    }

    Map<String, String> messageToMap(InputStream inputStream) {
        try {
            String str = readInputStream(inputStream);
            Map<String, String> map = convertToMap(str);
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    List<Message> mapMessage(Socket socket) {
        List<Message> messages = new ArrayList<>();
        try {
            InputStream inputStream = socket.getInputStream();
            while (inputStream.available() > 0) {
                boolean clientRequireAuth = false;
                Map<String, String> map = messageToMap(socket.getInputStream());
                String sender;
                String receiver;
                String messageStr = null;
                User user = null;
                AuthenticationManager authenticationManager = new AuthenticationManager();
                if (map.containsKey("from")) {
                    sender = map.get("from");
                    if (map.containsKey("authenticateMe")) {
                        User u = authenticationManager.generateTokenForNewUser(sender, socket);
                        authenticationManager.addUser(u);
                        String token = String.format("authentication = %s", u.getToken());
                        token = String.format("%d;%s",token.getBytes().length,token);
                        socket.getOutputStream().write(token.getBytes());
                        socket.getOutputStream().flush();
                        clientRequireAuth = true;
                    }
                } else {
                    throw new IllegalMessage("No sender!");
                }
                if (!clientRequireAuth) {
                    if (map.containsKey("to")) {
                        receiver = map.get("to");
                    } else {
                        throw new IllegalMessage("No receiver!");
                    }
                    if (map.containsKey("auth")) {
                        user = new User(sender, map.get("auth"), socket);
                        boolean authentication = authenticationManager.authenticate(user);
                        if (!authentication) {
                            throw new IllegalAuthorization();
                        }
                    } else {

                    }
                    if (map.containsKey("message")) {
                        messageStr = map.get("message");
                    }

                    Message message = new Message(user, receiver);
                    message.setMessage(messageStr);
                    messages.add(message);
                }
            }
        } catch (IOException | IllegalMessage | IllegalAuthorization e) {
            e.printStackTrace();
        }
        return messages;
    }

    private Map<String, String> convertToMap(String str) {
        Map<String, String> map = new HashMap<>();
        String[] lines = str.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String[] line = lines[i].split("=");
            String key = line[0].trim();
            String value = line[1].trim();
            map.put(key, value);
        }
        return map;
    }

     String readInputStream(InputStream inputStream) throws IOException {
        int inputLength = readMessageLength(inputStream);
        return readInput(inputStream,inputLength);
    }

    private String readInput(InputStream inputStream, int messageLength) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < messageLength; i++) {
            char c =(char) inputStream.read();
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    private int readMessageLength(InputStream inputStream) throws IOException {
        if (inputStream.available() > 0) {
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
}
