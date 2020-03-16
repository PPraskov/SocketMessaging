package messaging.messages;

import messaging.constants.MessageConstants;

class MessageConverter {
    private static MessageConverter messageConverter;
    private static volatile boolean alive = false;

    private MessageConverter() {
        alive = true;
    }

    static MessageConverter getMessageConverter() {
        MessageConverter converter;
        if (!alive) {
            createConverter();
        }
        converter = messageConverter;
        return converter;
    }

    private static void createConverter() {
        messageConverter = new MessageConverter();
    }

    String convertMessage(String... message) {
        StringBuilder stringBuilder = new StringBuilder();
        int messageLines = message.length;
        stringBuilder.append(messageLines + ";");
        for (int i = 0; i < messageLines; i++) {
            stringBuilder.append(String.format(MessageConstants.OUTPUT_MESSAGE_PATTERN_PART,getBytesLength(message[i]) , message[i]));
        }
        return stringBuilder.toString();
    }

    private int getBytesLength(String message) {
        return message.getBytes().length;
    }
}
