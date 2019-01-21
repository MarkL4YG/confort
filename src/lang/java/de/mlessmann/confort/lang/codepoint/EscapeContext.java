package de.mlessmann.confort.lang.codepoint;

public abstract class EscapeContext {

    protected StringBuilder result = new StringBuilder();
    protected StringBuilder escapeBuffer = new StringBuilder();
    protected boolean escapeMode = false;

    public void acceptUnescape(final char c) {
        if (escapeMode) {
            if (isEscapeChar(c)) {
                result.append(c);
                escapeMode = false;

            } else {
                aggregateEscape(c);
            }
        } else {
            if (isEscapeChar(c)) {
                escapeMode = true;
            } else {
                result.append(c);
            }
        }
    }

    protected abstract boolean isEscapeChar(final char c);

    protected abstract void aggregateEscape(final char c);

    public void acceptEscape(final char c) {
        if (shouldEscape(c)) {
            escapeChar(c);

        } else {
            result.append(c);
        }
    }

    protected abstract boolean shouldEscape(final char c);

    protected abstract void escapeChar(final char c);

    public String flush() {
        return result.toString();
    }
}
