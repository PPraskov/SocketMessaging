package messaging;

import java.net.Socket;
import java.util.List;

class SocketCommit extends Thread {

    private final Socket socket;

    SocketCommit(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        InputProcessor processor = new InputProcessor();
        List<Message> messages = processor.mapMessage(this.socket);
        if (messages.size() > 0) {
            MessageQueue queue = new MessageQueue();
            queue.addAllMessages(messages);
        }
    }
}
