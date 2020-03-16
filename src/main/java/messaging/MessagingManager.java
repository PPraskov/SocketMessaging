package messaging;

import messaging.constants.ServerConstants;
import messaging.maintenance.MemoryMonitor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MessagingManager {

    private ServerSocket serverSocket;
    private boolean toRun = false;
    private SocketInputListener inputListener;
    private MessageSender sender;
    private MemoryMonitor monitor;

    public MessagingManager() {
    }

    public void initializeAndStart() {
        try {
            this.serverSocket = new ServerSocket(ServerConstants.PORT);
            toRun = true;
            this.monitor = new MemoryMonitor();
            this.monitor.start();
            execute();
        } catch (IOException e) {
            System.out.println(ServerConstants.INITIALIZATION_PROBLEM);
            e.printStackTrace();
        }
    }

    private void execute() {
        try {
            this.inputListener = new SocketInputListener(this.monitor);
            this.sender = new MessageSender(this.monitor);
            this.inputListener.start();
            this.sender.start();
            while (this.toRun) {
                Socket socket = this.serverSocket.accept();
                socket.setSoTimeout(ServerConstants.TIMEOUT);
                InitialSocketAuthentication initialSocketAuthentication = new InitialAuthentication(socket);
                new Thread(initialSocketAuthentication).start();
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
