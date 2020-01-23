package messaging;

class Message {

    private final User sender;
    private final String receiver;
    private String message;
    private String auth;

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

    String getAuth() {
        return auth;
    }

    void setAuth(String auth) {
        this.auth = auth;
    }

    String convertToString(User receiver){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("from = %s",getSender().getUsername())).append(System.lineSeparator());
        stringBuilder.append(String.format("message = %s",getMessage())).append(System.lineSeparator());
        return stringBuilder.toString().trim();
    }
}
