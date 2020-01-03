package de.mlessmann.confort.lang;

import org.antlr.v4.runtime.*;

/**
 * @implNote Bits of code taken from {@link DiagnosticErrorListener}.
 */
public class AntlrErrorListener extends DiagnosticErrorListener {

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        String expectedTokens = null;
        if (e != null) {
            expectedTokens = e.getExpectedTokens().toString(recognizer.getVocabulary());
        }
        Token offendingToken = null;
        if (offendingSymbol instanceof CommonToken) {
            offendingToken = ((Token) offendingSymbol);
        } else if (e != null) {
            offendingToken = e.getOffendingToken();
        }

        String offendingPart;
        String sourceName;
        if (offendingToken != null) {
            offendingPart = offendingToken.getText();
            sourceName = offendingToken.getTokenSource().getSourceName();
        } else {
            offendingPart = offendingSymbol != null ? offendingSymbol.toString() : "<null>";
            sourceName = "<unknown>";
        }

        String message;
        if (expectedTokens != null) {
            message = String.format("Syntax error at token \"%s\", expected: \"%s\"! (ParserMsg: %s)",
                    offendingPart,
                    expectedTokens,
                    msg);
        } else {
            message = String.format("Syntax error at token \"%s\"! (ParserMsg: %s)", offendingPart, msg);
        }

        throw new RuntimeParseException(
                line,
                charPositionInLine,
                sourceName,
                message,
                e
        );
    }
}
