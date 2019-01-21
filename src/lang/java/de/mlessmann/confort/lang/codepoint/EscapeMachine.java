package de.mlessmann.confort.lang.codepoint;

public abstract class EscapeMachine {

    public String unescape(final String input) {
        final EscapeContext ctx = produceContext();
        int len = input.length();
        for (int i = 0; i < len; i++) {
            ctx.acceptUnescape(input.charAt(i));
        }
        return ctx.flush();
    }

    public String escape(final String input) {
        final EscapeContext ctx = produceContext();
        int len = input.length();
        for (int i = 0; i < len; i++) {
            ctx.acceptEscape(input.charAt(i));
        }
        return ctx.flush();
    }

    protected abstract EscapeContext produceContext();
}
