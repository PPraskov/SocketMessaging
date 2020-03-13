package messaging;

import java.io.IOException;
import java.net.Socket;

public interface InitialSocketAuthentication {

    void authenticateSocket(Socket socket) throws IOException;
}
