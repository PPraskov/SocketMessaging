package messaging;

class Message {

    private final User sender;
    private final String receiver;
    private final byte[] message;

    Message(User sender, String receiver, byte[] message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    byte[] getMessage() {
        return message;
    }

    User getSender() {
        return sender;
    }

    String getReceiver() {
        return receiver;
    }

    byte[] convertToByteArr() {
        int senderLength = this.sender.getUsername().getBytes().length;
        String messageStringPart = String.format(MessageConstants.OUTPUT_MESSAGE_PATTERN_PART+"%d;",
                senderLength,
                this.sender.getUsername(),
                this.message.length);

        return combineMessageAndString(messageStringPart.getBytes());
    }

    private byte[] combineMessageAndString(byte[] messageFromString) {
        int length = this.message.length + messageFromString.length;
        byte[] arr = new byte[length];
        int i = 0;
        for (byte b : messageFromString
        ) {
            arr[i] = b;
            i++;
        }
        for (byte b : this.message
        ) {
            arr[i] = b;
            i++;
        }
        return arr;
    }
}
