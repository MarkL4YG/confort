package de.mlessmann.confort.lang.json;

import de.mlessmann.confort.api.except.EscapeMachineException;
import de.mlessmann.confort.lang.codepoint.EscapeContext;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class JSONEscapeContext extends EscapeContext {

    private static final Charset JSON_CHARSET = Charset.forName("UTF-8");
    protected boolean unicodeMode = false;

    @Override
    protected boolean isEscapeChar(char c) {
        return c == '\\';
    }

    @Override
    protected void aggregateEscape(char c) {
        if (!unicodeMode) {
            switch (c) {
                case 'n':
                    result.append('\n');
                    escapeMode = false;
                    break;
                case 'r':
                    result.append('\r');
                    escapeMode = false;
                    break;
                case 't':
                    result.append('\t');
                    break;
                case 'f':
                    result.append('\f');
                    break;
                case 'b':
                    result.append('\b');
                    break;
                case '/':
                case '\\':
                case '"':
                    result.append(c);
                    break;
                case 'u':
                    unicodeMode = true;
                    break;

                    default:
                        throw new EscapeMachineException("Invalid character in escape sequence: " + c);
            }
        } else {
            if (c >= '0' && c <= '9') {
                escapeBuffer.append(c);
            } else {
                String unicodeNumberStr = escapeBuffer.toString();
                result.append(deserializeUnicode(unicodeNumberStr));

                escapeBuffer = new StringBuilder();
                unicodeMode = false;
                result.append(c);
            }
        }
    }

    @Override
    protected boolean shouldEscape(char c) {
        switch (c) {
            case '\n':
            case '\r':
            case '\t':
            case '\f':
            case '\b':
            case '/':
            case '\\':
            case '"':
                return true;

            default:
                return c < ' ';
        }
    }

    @Override
    protected void escapeChar(char c) {

        switch (c) {
            case '\n':
                result.append("\\n");
                break;
            case '\r':
                result.append("\\r");
                break;
            case '\t':
                result.append("\\t");
                break;
            case '\f':
                result.append("\\f");
                break;
            case '\b':
                result.append("\\b");
                break;

            default:
                if (c < ' ') {
                    result.append(String.format("\\u%02X", (int) c));
                } else {
                    result.append("\\").append(c);
                }
        }
    }

    private char deserializeUnicode(String unicodeNumberStr) {
        return Character.toChars(Integer.parseInt(unicodeNumberStr, 16))[0];
    }

    @Override
    public String flush() {
        if (unicodeMode) {
            result.append(deserializeUnicode(escapeBuffer.toString()));
            escapeBuffer = new StringBuilder();
            unicodeMode = false;
        }
        return super.flush();
    }
}
