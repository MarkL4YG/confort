package de.mlessmann.confort.api.except;

public class EscapeMachineException extends RuntimeException {

    public EscapeMachineException() {
        super();
    }

    public EscapeMachineException(String message) {
        super(message);
    }

    public EscapeMachineException(String message, Throwable cause) {
        super(message, cause);
    }

    public EscapeMachineException(Throwable cause) {
        super(cause);
    }

    protected EscapeMachineException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
