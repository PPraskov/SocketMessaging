public class Message implements Comparable<Message>{

    private String from;
    private String to;
    private String content;

    public Message(String from, String to, String content) {
        this.from = from;
        this.to = to;
        this.content = content;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getContent() {
        return content;
    }

    @Override
    public int compareTo(Message message) {
        boolean sender = false;
        boolean receiver = false;
        boolean content = false;
        if (this.from.equals(message.from)){
            sender = true;
        }
        if (this.to.equals(message.to)){
            receiver = true;
        }
        if (this.to.equals(message.to)){
            content = true;
        }
        if (sender && receiver && content){
            return 0;
        }
        return -1;
    }
}
