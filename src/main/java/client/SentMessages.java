package client;

import client.messages.OutputMessage;

import java.util.ArrayList;
import java.util.List;

public class SentMessages {

    private final List<OutputMessage> sent;

    SentMessages() {
        sent = new ArrayList<>();
    }

    void addMessage(OutputMessage messageSending){
        sent.add(messageSending);
    }

    public List<OutputMessage> getAllSentMessages() {
        return sent;
    }
}
