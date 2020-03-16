package messaging;

import java.io.IOException;
import java.net.Socket;

public interface InitialSocketAuthentication extends Runnable{

    void authenticateSocket(Socket socket) throws IOException;
}
