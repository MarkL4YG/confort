package de.mlessmann.confort.lang;

import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.DiagnosticErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

/**
 * @implNote Bits of code taken from {@link DiagnosticErrorListener}.
 */
public class AntlrErrorListener extends DiagnosticErrorListener {

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        String expectedTokens = e.getExpectedTokens().toString(recognizer.getVocabulary());
        String offendingPart;
        if (offendingSymbol instanceof CommonToken) {
            offendingPart = ((CommonToken) offendingSymbol).getText();
        } else {
            offendingPart = offendingSymbol != null ? offendingSymbol.toString() : "<null>";
        }

        throw new RuntimeParseException(
                line,
                charPositionInLine,
                e.getOffendingToken().getTokenSource().getSourceName(),
                String.format("Syntax error! Got \"%s\" expected: %s", offendingPart, expectedTokens),
                e
        );
    }
}
