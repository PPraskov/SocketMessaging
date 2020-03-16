package client.messages;

public class MessageReceiving extends InputMessage {

    public MessageReceiving(String from, String message) {
        super();
        this.setFrom(from);
        this.setMessage(message);
    }

    @Override
    public boolean equals(Object obj) {
        boolean equals = false;
        MessageReceiving messageReceiving = (MessageReceiving) obj;
        if (this.getFrom().equals(messageReceiving.getFrom()) &&
                this.getMessage().equals(messageReceiving.getMessage())) {
            equals = true;
        }
        return equals;
    }



}
