package de.mlessmann.confort.migration.nodes;

public class OpArgument {

    private boolean isNode;
    private boolean relative;
    private String identifier;

    public OpArgument(String generatorName) {
        this.identifier = generatorName;
    }

    public OpArgument(boolean relative, String identifier) {
        this.relative = relative;
        this.identifier = identifier;
        this.isNode = true;
    }

    public boolean isNodeReference() {
        return isNode;
    }

    public boolean isRelative() {
        return relative;
    }

    public String getIdentifier() {
        return identifier;
    }
}
