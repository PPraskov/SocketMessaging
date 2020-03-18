package messaging.constants;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

public interface MessageConstants {
    int MESSAGE_TYPE_AUTHENTICATION = 4;
    int MESSAGE_TYPE_COMMAND = 5;
    int MESSAGE_TYPE_CONTACT = 6;
    String OUTPUT_MESSAGE_PATTERN_PART = "%d;%s";
    Charset ENCODING = StandardCharsets.UTF_8;
    String NO_USER_FOUND = "User not found!";
    String MESSAGE_SERVER = "Messaging Server";
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
}
