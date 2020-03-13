package messaging;

import java.io.IOException;
import java.io.OutputStream;

class OutputMessageWriter {
    private static OutputMessageWriter writer;
    private static volatile boolean alive = false;

    static {
        writer = new OutputMessageWriter();
        alive = true;
    }
    private OutputMessageWriter(){}

    public static OutputMessageWriter getWriter() {
        OutputMessageWriter outputMessageWriter;
        if (!alive){
            createWriter();
        }
        outputMessageWriter = writer;
        return outputMessageWriter;
    }

    private static void createWriter() {
        writer = new OutputMessageWriter();
    }

     void flushMessage(OutputStream outputStream,String message) throws IOException {
        outputStream.write(message.getBytes());
        outputStream.flush();
    }

    void flushMessage(OutputStream outputStream, byte[] arr) throws IOException {
        outputStream.write(arr);
        outputStream.flush();
    }
}
