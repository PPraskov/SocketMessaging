package messaging;

import java.net.Socket;

class SocketCommit extends Thread {

    private final Socket socket;

    SocketCommit(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        InputProcessor processor = new InputProcessor();
        Message message = processor.mapMessage(this.socket);
        if (message != null) {
            MessageQueue queue = new MessageQueue();
            queue.addMessage(message);
        }
    }
}
