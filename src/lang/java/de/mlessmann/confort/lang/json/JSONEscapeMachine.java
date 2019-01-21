package de.mlessmann.confort.lang.json;

import de.mlessmann.confort.lang.codepoint.EscapeContext;
import de.mlessmann.confort.lang.codepoint.EscapeMachine;

public class JSONEscapeMachine extends EscapeMachine {

    @Override
    protected EscapeContext produceContext() {
        return new JSONEscapeContext();
    }
}
