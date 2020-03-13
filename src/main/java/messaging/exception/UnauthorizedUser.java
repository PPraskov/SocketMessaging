package messaging.exception;

public class UnauthorizedUser extends RuntimeException {

    public UnauthorizedUser(String username){
        super("Illegal authorization!" + username);
    }
}
