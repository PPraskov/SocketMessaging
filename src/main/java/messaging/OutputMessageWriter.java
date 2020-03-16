package messaging;

import messaging.constants.MessageConstants;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

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
         flushMessage(outputStream,message.toCharArray());
    }

    void flushMessage(OutputStream outputStream, char[] arr) throws IOException {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, MessageConstants.ENCODING);
        outputStreamWriter.write(arr);
        outputStreamWriter.flush();
    }
}
