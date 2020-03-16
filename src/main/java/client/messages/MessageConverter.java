package client.messages;

import client.constants.MessageConstants;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

public class MessageConverter {
    private static MessageConverter messageConverter;
    private static boolean alive = false;


    private MessageConverter() {
    }

    public static MessageConverter getMessageConverter() {
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
            stringBuilder.append(String.format(MessageConstants.PATTERN,getBytesLength(message[i]) , message[i]));
        }
        return stringBuilder.toString();
    }

    private int getBytesLength(String message) {
        return message.getBytes().length;
    }
}
