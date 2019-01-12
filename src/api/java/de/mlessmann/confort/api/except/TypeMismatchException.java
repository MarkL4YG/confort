package de.mlessmann.confort.api.except;

/**
 * Thrown when a value is tried to be retrieved from a {@link de.mlessmann.confort.api.IValueHolder}
 * but the actually stored value does not match the required type.
 */
public class TypeMismatchException extends RuntimeException {

    public TypeMismatchException() {
    }

    public TypeMismatchException(String message) {
        super(message);
    }

    public TypeMismatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public TypeMismatchException(Throwable cause) {
        super(cause);
    }

    public TypeMismatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
