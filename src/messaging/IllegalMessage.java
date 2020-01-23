package messaging;

public class IllegalMessage extends RuntimeException {

    public IllegalMessage(String message){
        super(message);
    }
}
