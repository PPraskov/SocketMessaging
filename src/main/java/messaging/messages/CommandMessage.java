package messaging.messages;

import messaging.constants.MessageConstants;

import java.net.Socket;

public class CommandMessage extends AbstractMessage {

    private State executionState;

    CommandMessage(String username,String password, String authenticationToken, String command, String dateTimeAsString, Socket socket) {
        super(username,password, authenticationToken, null, command, dateTimeAsString, socket);
        this.executionState = State.PENDING;
    }

    CommandMessage(){

    }
    public String getCommand() {
        return super.getMessage();
    }

    public State getState() {
        return executionState;
    }

    public void setState(boolean successfulExecution) {
        if (successfulExecution) {
            this.executionState = State.SUCCESSFUL;
        } else {
            this.executionState = State.UNSUCCESSFUL;
        }
    }

    @Override
    public String convertMessage() {
        return MessageConverter.getMessageConverter().
                convertMessage(MessageConstants.MESSAGE_SERVER, getState().getStateString(),getDateTimeAsString());
    }

    private enum State {
        PENDING("Pending"), SUCCESSFUL("Successfully executed"), UNSUCCESSFUL("Not executed!");
        private final String stateString;

        State(String message) {
            this.stateString = message;
        }

         String getStateString() {
            return stateString;
        }
    }
}
