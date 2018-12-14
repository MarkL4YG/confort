package de.mlessmann.confort.lang;

public class ParseVisitException extends RuntimeException {

    public ParseVisitException() {
    }

    public ParseVisitException(String message) {
        super(message);
    }

    public ParseVisitException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParseVisitException(Throwable cause) {
        super(cause);
    }

    public ParseVisitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
