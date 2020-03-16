package client.messages;

import client.constants.MessageConstants;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

public abstract class OutputMessage extends Message {
    OutputMessage(){
        super();
   }

   public abstract String convertMessage() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;

    String getAndSetTime(){
        setDateTimeAsString(MessageConstants.dtf.format(LocalDateTime.now()));
        return getDateTimeAsString();
    }
}
