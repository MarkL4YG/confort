package de.mlessmann.confort.lang;

import org.antlr.v4.runtime.tree.ParseTree;

public class UnmatchedContextException extends ParseVisitException {

    private ParseTree context;

    public UnmatchedContextException() {
    }

    public UnmatchedContextException(String message) {
        super(message);
    }

    public UnmatchedContextException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnmatchedContextException(Throwable cause) {
        super(cause);
    }

    public UnmatchedContextException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ParseTree getContext() {
        return context;
    }

    public void setContext(ParseTree context) {
        this.context = context;
    }
}
