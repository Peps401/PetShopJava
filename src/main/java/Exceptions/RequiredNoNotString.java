package Exceptions;

public class RequiredNoNotString extends RuntimeException{
    public RequiredNoNotString() {
    }

    public RequiredNoNotString(String message) {
        super(message);
    }

    public RequiredNoNotString(String message, Throwable cause) {
        super(message, cause);
    }

    public RequiredNoNotString(Throwable cause) {
        super(cause);
    }
}
