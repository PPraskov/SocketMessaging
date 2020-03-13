package client;

abstract class  Message {
    static final String MESSAGE_END = "&&#";
    static final String LENTGH_PART = "%d;%s";

    Message(){

   }

   abstract byte[] convertToByteArr();

}
