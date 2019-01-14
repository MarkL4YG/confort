package de.mlessmann.confort.api.except;

public class LoaderLookupException extends RuntimeException {

    public LoaderLookupException() {
    }

    public LoaderLookupException(String message) {
        super(message);
    }

    public LoaderLookupException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoaderLookupException(Throwable cause) {
        super(cause);
    }

    public LoaderLookupException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
