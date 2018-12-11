package de.mlessmann.confort.migration.nodes.operations;

public enum OpCode {
    DROP(false),
    MOVE(true),
    MERGE_APPEND(true),
    MERGE_PREPEND(true),
    GENERATE(true),
    CONSUME(true);

    private boolean hasArguments;

    public boolean hasArguments() {
        return hasArguments;
    }

    OpCode(boolean hasArguments) {
        this.hasArguments = hasArguments;
    }
}
