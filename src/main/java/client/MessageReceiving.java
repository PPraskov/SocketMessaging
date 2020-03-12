package client;

public class MessageReceiving {
    private String from;
    private String message;

    public MessageReceiving(String from, String message) {
        this.from = from;
        this.message = message;
    }

    @Override
    public boolean equals(Object obj) {
        boolean equals = false;
        MessageReceiving messageReceiving =(MessageReceiving) obj;
        if (this.from.equals(messageReceiving.from) && this.message.equals(messageReceiving.message)){
            equals = true;
        }
        return equals;
    }

    public String getFrom() {
        return from;
    }

    public String getMessage() {
        return message;
    }
}
