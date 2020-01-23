package messaging;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MessagingManager {

    private ServerSocket serverSocket;
    private boolean toRun = false;

    public MessagingManager() {
    }

    public void initializeAndStart() {
        try {
            this.serverSocket = new ServerSocket(3600);
            toRun = true;
            execute();
        } catch (IOException e) {
            System.out.println("Messaging initialization problem");
            e.printStackTrace();
        }
    }

    private void execute() {
        try {
            SocketInputListener listener = new SocketInputListener();
            MessageSender sender = new MessageSender();
            listener.start();
            sender.start();
            while (this.toRun) {
                Socket socket = this.serverSocket.accept();
                new SocketCommit(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
