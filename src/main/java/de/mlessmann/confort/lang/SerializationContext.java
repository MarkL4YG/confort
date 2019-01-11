package de.mlessmann.confort.lang;

import de.mlessmann.confort.api.lang.ISerializationContext;

public class SerializationContext implements ISerializationContext {

    private Integer currentIndentLevel = 0;

    @Override
    public Integer getCurrentIndentLevel() {
        return currentIndentLevel;
    }

    @Override
    public void indentIncrement() {
        currentIndentLevel++;
    }

    @Override
    public void indentDecrement() {
        currentIndentLevel--;
    }
}
