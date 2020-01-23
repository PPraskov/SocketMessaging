public class TestMessageReceiving {
    private String from;
    private String message;

    public TestMessageReceiving(String from, String message) {
        this.from = from;
        this.message = message;
    }

    @Override
    public boolean equals(Object obj) {
        boolean equals = false;
        TestMessageReceiving messageReceiving =(TestMessageReceiving) obj;
        if (this.from.equals(messageReceiving.from) && this.message.equals(messageReceiving.message)){
            equals = true;
        }
        return equals;
    }
}
