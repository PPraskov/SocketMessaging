package messaging.constants;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public interface MessageConstants {
    int MESSAGE_TYPE_AUTHENTICATION = 3;
    int MESSAGE_TYPE_COMMAND = 4;
    int MESSAGE_TYPE_CONTACT = 5;
    String OUTPUT_MESSAGE_PATTERN_PART = "%d;%s";
    Charset ENCODING = StandardCharsets.UTF_8;
    String NO_USER_FOUND = "User not found!";
    String MESSAGE_SERVER = "Messaging Server";
}
