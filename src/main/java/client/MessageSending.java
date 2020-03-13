package client;

public class MessageSending extends Message{

    private final String from;
    private final String auth;
    private final String to;
    private final String message;

    MessageSending(User user , String to, String message) {
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
    byte[] convertToByteArr(){
        int fromLength = this.from.getBytes().length;
        int authLength = this.auth.getBytes().length;
        int toLength = this.to.getBytes().length;
        String result = String.format(LENTGH_PART+LENTGH_PART+LENTGH_PART+"%d;",
                fromLength,
                this.from,
                authLength,
                this.auth,
                toLength,
                this.to,
                this.message.getBytes().length);
        return combineMessageAndString(result.getBytes());
    }
    private byte[] combineMessageAndString(byte[] messageFromString) {
        byte[] messageBytes = this.message.getBytes();
        int length = messageBytes.length + messageFromString.length;
        byte[] arr = new byte[length];
        int i = 0;
        for (byte b : messageFromString
        ) {
            arr[i] = b;
            i++;
        }
        for (byte b : messageBytes
        ) {
            arr[i] = b;
            i++;
        }
        return arr;
    }
}
