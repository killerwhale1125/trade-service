package carrot.market.exception;

public class RefreshTokenMismatchException extends RuntimeException {
    public RefreshTokenMismatchException() {
        super();
    }

    public RefreshTokenMismatchException(String message) {
        super(message);
    }

    public RefreshTokenMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
