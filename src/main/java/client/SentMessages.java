package client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SentMessages {

    private final List<MessageSending> sent;

    SentMessages() {
        sent = new ArrayList<>();
    }

    void addMessage(MessageSending messageSending){
        sent.add(messageSending);
    }

    public List<MessageSending> getAllSentMessages() {
        return sent;
    }
}
