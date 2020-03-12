package messaging;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class MessagingManager {

    private ServerSocket serverSocket;
    private boolean toRun = false;
    private SocketInputListener inputListener;
    private MessageSender sender;

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
            this.inputListener = new SocketInputListener();
            this.sender = new MessageSender();
            this.inputListener.start();
            this.sender.start();
            System.out.println("App is running!");
            while (this.toRun) {
                Socket socket = this.serverSocket.accept();
                socket.setSoTimeout(1000);
                new SocketCommit(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopApplication(){
        try {
        this.toRun = false;
        this.inputListener.stopRunning();
        this.sender.stopRunning();
        new MemoryMonitor().start();
            this.serverSocket.close();
        } catch (IOException e) {
            System.out.println("Server socket is closed!");
        }
    }

}
