import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TestUser {

    private String name;
    private String auth;
    private Socket socket;

    public TestUser(String name) {
        this.name = name;
        try {
            this.socket = new Socket("localhost", 3600);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public void requireAuth(){
        try {
            String toSend = String.format("from = %s\nauthenticateMe = please",this.name);
            OutputStream outputStream = this.socket.getOutputStream();
            outputStream.write(toSend.getBytes());
            outputStream.flush();
            Thread.sleep(100);
            String received = readInputStream(this.socket.getInputStream());
            if (received.startsWith("authentication =")){
                String[] arr = received.split("=");
                this.setAuth(arr[1].trim());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String sendTo, String message) throws IOException {
        TestMessageSending messageSending = new TestMessageSending(this,sendTo,message);
        String messageToStr = messageSending.toString();
        OutputStream outputStream = this.socket.getOutputStream();
        outputStream.write(messageToStr.getBytes());
        outputStream.flush();
    }

    public TestMessageReceiving receiveMessage() throws IOException, InterruptedException {
        String received = readInputStream(this.socket.getInputStream());
        return mapToMessage(received);
    }

    private TestMessageReceiving mapToMessage(String received) {
        String[] strings = received.split("\n");
        String[] arg = new String[2];
        for (int i = 0; i < strings.length; i++) {
            String[] split = strings[i].split("=");
            arg[i] = split[1].trim();
        }
        return new TestMessageReceiving(arg[0],arg[1]);
    }

    private String readInputStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        boolean read = false;
        while (!read){
            if (inputStream.available() == 0){
                continue;
            }
            while (inputStream.available() > 0) {
                stringBuilder.append((char) inputStream.read());
            }
            read = true;
        }
        return stringBuilder.toString();
    }

}
