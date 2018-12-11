package de.mlessmann.confort.migration.nodes;

import java.util.regex.Pattern;

public class OpArgument {

    private static final Pattern PATH_DELIM = Pattern.compile("(?!\\\\)\\.");

    private boolean isNode;
    private boolean relative;
    private String identifier;
    private String[] path;

    public OpArgument(String generatorName) {
        this.identifier = generatorName;
        this.path = PATH_DELIM.split(identifier);
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

    public String[] asPath() {
        return path;
    }
}
