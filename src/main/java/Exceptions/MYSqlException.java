package Exceptions;

public class MYSqlException extends Exception{
    public MYSqlException() {
    }

    public MYSqlException(String message) {
        super(message);
    }

    public MYSqlException(String message, Throwable cause) {
        super(message, cause);
    }

    public MYSqlException(Throwable cause) {
        super(cause);
    }
}
