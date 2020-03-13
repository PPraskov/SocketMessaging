package messaging;

class MessageFactory {
    private static MessageFactory factory;
    private static volatile boolean alive = false;

    static {
        factory = new MessageFactory();
        alive = true;
    }

    private MessageFactory() {

    }

    public static MessageFactory getFactory() {
        MessageFactory messageFactory;
        if (!alive) {
            createFactory();
        }
        messageFactory = factory;
        return messageFactory;
    }

    private static void createFactory() {
        factory = new MessageFactory();
        alive = true;
    }

    Message createMessage(User sender, String to, byte[] message){
        return new Message(sender,to,message);
    }
}
