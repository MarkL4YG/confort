package de.mlessmann.confort.lang.except;

public class FileFormatException extends RuntimeException {

    private int linePosition;
    private int columnPosition;
    private String sourceLocation;
    private String englishMessage;

    public FileFormatException(int linePosition, int columnPosition, String sourceLocation, String englishMessage) {
        super();
        this.linePosition = linePosition;
        this.columnPosition = columnPosition;
        this.sourceLocation = sourceLocation;
        this.englishMessage = englishMessage;
    }

    public FileFormatException(int linePosition, int columnPosition, String sourceLocation, String englishMessage, Throwable throwable) {
        super(throwable);
        this.linePosition = linePosition;
        this.columnPosition = columnPosition;
        this.sourceLocation = sourceLocation;
        this.englishMessage = englishMessage;
    }

    @Override
    public String getMessage() {
        return String.format("%s (At %s -> %s:%s)", englishMessage, sourceLocation, linePosition, columnPosition);
    }

    @Override
    public String getLocalizedMessage() {
        return super.getMessage();
    }
}
