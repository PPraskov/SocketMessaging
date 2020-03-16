package client.exceptions;

public class AuthorizationProblem extends RuntimeException {

    public static final String WRONG_USERNAME_RECEIVED = "Wrong username received!";

    public AuthorizationProblem(String message){
        super(message);
    }
}
