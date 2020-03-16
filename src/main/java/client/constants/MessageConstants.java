package client.constants;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

public interface MessageConstants {
    String PATTERN = "%d;%s";
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    String AUTHENTICATION_REQUEST_MESSAGE = "reqAuth";
    Charset ENCODING = StandardCharsets.UTF_8;

}
