package Exceptions;

public class RequiredStringNotNoExc extends RuntimeException{
    public RequiredStringNotNoExc() {
    }

    public RequiredStringNotNoExc(String message) {
        super(message);
    }

    public RequiredStringNotNoExc(String message, Throwable cause) {
        super(message, cause);
    }

    public RequiredStringNotNoExc(Throwable cause) {
        super(cause);
    }
}
