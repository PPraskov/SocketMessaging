package messaging;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

class SocketCommit extends Thread {

    private final Socket socket;

    SocketCommit(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        InputProcessor processor = new InputProcessor();
        while (true){
            try {
                InputStream stream = this.socket.getInputStream();
                if (stream.available() > 0){
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Message message = processor.mapMessage(this.socket);
        if (message != null) {
            MessageQueue queue = new MessageQueue();
            queue.addMessage(message);
        }
    }
}
