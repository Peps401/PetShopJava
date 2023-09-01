package Exceptions;

public class MYIOException extends Exception{
    public MYIOException() {
    }

    public MYIOException(String message) {
        super(message);
    }

    public MYIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public MYIOException(Throwable cause) {
        super(cause);
    }
}
