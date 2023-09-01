package Exceptions;

public class ObjectWithThatIdNotFoundException extends RuntimeException {
    public ObjectWithThatIdNotFoundException() {
    }

    public ObjectWithThatIdNotFoundException(String message) {
        super(message);
    }

    public ObjectWithThatIdNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectWithThatIdNotFoundException(Throwable cause) {
        super(cause);
    }
}
