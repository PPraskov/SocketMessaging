package messaging.exception;

public class IllegalAuthorization extends RuntimeException {

    public IllegalAuthorization(){
        super("Illegal authorization!");
    }
}
