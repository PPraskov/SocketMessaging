package messaging.exception;

public class UnrecognizedMessage extends RuntimeException {

    private static final String MESSAGE = "Unrecognized Message";

    public UnrecognizedMessage() {
        super(MESSAGE);
    }
}
