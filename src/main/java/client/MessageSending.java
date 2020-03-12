package client;

public class MessageSending {

    private final String from;
    private final String auth;
    private final String to;
    private final String message;
    private int byteSize;

    public MessageSending(User user , String to, String message) {
        this.from = user.getName();
        this.auth = user.getAuth();
        this.to = to;
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public String getAuth() {
        return auth;
    }

    public String getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("from =%s\n",this.from));
        stringBuilder.append(String.format("auth =%s\n",this.auth));
        stringBuilder.append(String.format("to =%s\n",this.to));
        stringBuilder.append(String.format("message =%s",this.message.trim()));
        String message = stringBuilder.toString();
        this.byteSize = message.getBytes().length;
        String result = String.format("%d;%s",this.byteSize,message);
        return result;
    }
}
