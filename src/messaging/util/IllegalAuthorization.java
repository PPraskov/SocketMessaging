package messaging.util;

public class IllegalAuthorization extends RuntimeException {

    public IllegalAuthorization(){
        super("Illegal authorization!");
    }
}
