package messaging;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MessagingManager {

    private ServerSocket serverSocket;
    private boolean toRun = false;
    private SocketInputListener inputListener;
    private MessageSender sender;
    private MemoryMonitor monitor;
    private InitialSocketAuthentication initialSocketAuthentication;

    public MessagingManager() {
    }

    public void initializeAndStart() {
        try {
            this.serverSocket = new ServerSocket(3600);
            toRun = true;
            this.monitor = new MemoryMonitor();
            this.initialSocketAuthentication = new InitialAuthentication();
            this.monitor.start();
            execute();
        } catch (IOException e) {
            System.out.println("Messaging initialization problem");
            e.printStackTrace();
        }
    }

    private void execute() {
        try {
            this.inputListener = new SocketInputListener(this.monitor);
            this.sender = new MessageSender(this.monitor);
            this.inputListener.start();
            this.sender.start();
            System.out.println("App is running!");
            while (this.toRun) {
                Socket socket = this.serverSocket.accept();
                socket.setSoTimeout(1000);
                this.initialSocketAuthentication.authenticateSocket(socket);
                this.monitor.checkForLockLock();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopApplication() {
        try {
            this.toRun = false;
            this.inputListener.stopRunning();
            this.sender.stopRunning();
            this.serverSocket.close();
        } catch (IOException e) {
            System.out.println("Server socket is closed!");
        }
    }

}
