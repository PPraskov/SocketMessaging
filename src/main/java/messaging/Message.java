package messaging;

class Message {

    private final User sender;
    private final String receiver;
    private String message;

    Message(User sender, String receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    String getMessage() {
        return message;
    }

    void setMessage(String message){
        this.message = message;
    }

    User getSender() {
        return sender;
    }

    String getReceiver() {
        return receiver;
    }

    String convertToString(){
       String message = String.format("from = %s", getSender().getUsername()) + System.lineSeparator() +
                String.format("message = %s", getMessage()) + System.lineSeparator();
       return String.format("%d;%s",getMessageLength(message),message);
    }

    private int getMessageLength(String message){
        return message.getBytes().length;
    }
}
