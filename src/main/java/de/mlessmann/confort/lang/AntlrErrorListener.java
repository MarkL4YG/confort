package de.mlessmann.confort.lang;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

import java.util.Arrays;
import java.util.BitSet;

public class AntlrErrorListener implements ANTLRErrorListener {

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

    @Override
    public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact, BitSet ambigAlts, ATNConfigSet configs) {
        throw new RuntimeParseException(
                0,
                0,
                "meow",
                "Ambiguous input!"
        );
    }

    @Override
    public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex, BitSet conflictingAlts, ATNConfigSet configs) {
        throw new RuntimeParseException(
                0,
                0,
                "meow",
                "Attempted full context!"
        );
    }

    @Override
    public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction, ATNConfigSet configs) {
        throw new RuntimeParseException(
                0,
                0,
                "meow",
                "Context sensitivity!"
        );
    }
}
