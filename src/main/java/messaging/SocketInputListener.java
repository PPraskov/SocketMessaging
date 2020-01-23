package messaging;

import messaging.authentication.ActiveUsersGetter;
import messaging.authentication.AuthenticationManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

class SocketInputListener extends Thread {

    private boolean toRun;

    SocketInputListener() {
        this.toRun = true;
    }

    @Override
    public void run() {
        ActiveUsersGetter activeUsersGetter = new AuthenticationManager();
        MessageQueue queue = new MessageQueue();
        while (this.toRun) {
            Map<String, User> activeUsers = activeUsersGetter.getActiveUsers();
            for (User user : activeUsers.values()
            ) {
                try {
                    InputStream inputStream = user.getSocket().getInputStream();
                    if (inputStream.available() > 0) {
                        InputProcessor processor = new InputProcessor();
                        Message message = processor.mapMessage(user.getSocket());
                        if (message != null) {
                            queue.addMessage(message);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    void stopRunning() {
        this.toRun = false;
    }
}
