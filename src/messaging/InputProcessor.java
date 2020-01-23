package messaging;

import messaging.authentication.AuthenticationManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
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

    Message mapMessage(Socket socket) {
        try {

            Map<String, String> map = messageToMap(socket.getInputStream());
            String sender = null;
            String receiver = null;
            String auth = null;
            String messageStr = null;
            User user = null;
            AuthenticationManager authenticationManager = new AuthenticationManager();
            if (map.containsKey("from")) {
                sender = map.get("from");
                if (map.containsKey("authenticateMe")) {
                    User u = authenticationManager.generateTokenForNewUser(sender, socket);
                    String token = String.format("authentication = %s", u.getToken());
                    socket.getOutputStream().write(token.getBytes());
                    socket.getOutputStream().flush();
                    throw new IllegalMessage("Sending Authorization");
                }
            } else {
                throw new IllegalMessage("No sender!");
            }
            if (map.containsKey("to")) {
                receiver = map.get("to");
            } else {
                throw new IllegalMessage("No receiver!");
            }
            if (map.containsKey("auth")) {
                auth = map.get("auth");
                user = new User(sender, auth, socket);
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
            message.setAuth(auth);
            return message;

        } catch (IOException | IllegalMessage | IllegalAuthorization e) {
            e.printStackTrace();
        }
        return null;
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

    private String readInputStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        while (inputStream.available() > 0) {
            char c = (char) inputStream.read();
            stringBuilder.append(c);
        }
        return stringBuilder.toString();

    }
}
