package de.mlessmann.confort.lang;

public class RuntimeParseException extends RuntimeException {
    private final int linePosition;
    private final int columnPosition;
    private final String sourceLocation;
    private final String englishMessage;

    public RuntimeParseException(int linePosition, int columnPosition, String sourceLocation, String englishMessage) {
        super();
        this.linePosition = linePosition;
        this.columnPosition = columnPosition;
        this.sourceLocation = sourceLocation;
        this.englishMessage = englishMessage;
    }

    public RuntimeParseException(int linePosition, int columnPosition, String sourceLocation, String englishMessage, Throwable throwable) {
        super(throwable);
        this.linePosition = linePosition;
        this.columnPosition = columnPosition;
        this.sourceLocation = sourceLocation;
        this.englishMessage = englishMessage;
    }

    @Override
    public String getMessage() {
        return String.format("%s (At \"%s\" -> %s:%s)", englishMessage, sourceLocation, linePosition, columnPosition);
    }

    @Override
    public String getLocalizedMessage() {
        return super.getMessage();
    }

    public int getLinePosition() {
        return linePosition;
    }

    public int getColumnPosition() {
        return columnPosition;
    }

    public String getSourceLocation() {
        return sourceLocation;
    }

    public String getEnglishMessage() {
        return englishMessage;
    }
}
