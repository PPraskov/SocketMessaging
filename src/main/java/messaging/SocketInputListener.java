package messaging;

import messaging.authentication.ActiveUsersGetter;
import messaging.authentication.AuthenticationManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

class SocketInputListener extends Thread {

    private boolean toRun;
    private MemoryMonitor monitor;

    SocketInputListener(MemoryMonitor monitor) {
        this.setDaemon(true);
        this.setPriority(9);
        this.monitor = monitor;
        this.toRun = true;
    }

    @Override
    public void run() {
        ActiveUsersGetter activeUsersGetter = AuthenticationManager.getManager();
        MessageQueue queue = MessageQueue.getQueue();
        while (this.toRun) {
            this.monitor.checkForLockLock();
            Map<String, User> activeUsers = activeUsersGetter.getActiveUsers();
            for (User user : activeUsers.values()
            ) {
                try {
                    InputStream inputStream = user.getSocket().getInputStream();
                    if (inputStream.available() > 0) {
                        InputProcessor processor = InputProcessor.getInputProcessor();
                        List<Message> messages = processor.mapMessage(user.getSocket());
                        queue.addAllMessages(messages);
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
