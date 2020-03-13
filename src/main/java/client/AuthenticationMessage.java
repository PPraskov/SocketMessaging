package client;
 class AuthenticationMessage extends Message{
    private final User user;
    private static final String AUTHENTICATION_REQUEST_MESSAGE = "reqAuth";
    private static int AUTHENTICATION_REQUEST_MESSAGE_BYTE_LENGTH;
    static {
        AUTHENTICATION_REQUEST_MESSAGE_BYTE_LENGTH = AUTHENTICATION_REQUEST_MESSAGE.getBytes().length;
    }
    AuthenticationMessage(User user) {
        this.user = user;
    }

    @Override
    byte[] convertToByteArr(){
        int usernameLength = this.user.getName().getBytes().length;
        String message = String.format(LENTGH_PART + LENTGH_PART,
                usernameLength,
                this.user.getName(),
                AUTHENTICATION_REQUEST_MESSAGE_BYTE_LENGTH,
                AUTHENTICATION_REQUEST_MESSAGE);
        return message.getBytes();
    }
}
