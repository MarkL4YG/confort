package de.mlessmann.confort.api.except;

/**
 * There was an error while trying to parse input.
 */
public class ParseException extends Exception {

    private final int linePosition;
    private final int columnPosition;
    private final String sourceLocation;
    private final String englishMessage;

    public ParseException(int linePosition, int columnPosition, String sourceLocation, String englishMessage) {
        super();
        this.linePosition = linePosition;
        this.columnPosition = columnPosition;
        this.sourceLocation = sourceLocation;
        this.englishMessage = englishMessage;
    }

    public ParseException(int linePosition, int columnPosition, String sourceLocation, String englishMessage, Throwable throwable) {
        super(throwable);
        this.linePosition = linePosition;
        this.columnPosition = columnPosition;
        this.sourceLocation = sourceLocation;
        this.englishMessage = englishMessage;
    }

    @Override
    public String getMessage() {
        return String.format("%s (At \"%s\"[%s:%s])", englishMessage, sourceLocation, linePosition, columnPosition);
    }

    @Override
    public String getLocalizedMessage() {
        return getMessage();
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
