package de.mlessmann.confort.lang.json;

import de.mlessmann.confort.api.IConfigNode;
import de.mlessmann.confort.api.lang.ISerializationContext;
import de.mlessmann.confort.lang.base.serializers.AbstractIndentAwareSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class JSONSerializer extends AbstractIndentAwareSerializer {

    private static final Pattern JSON_ESCAPE_CHARS = Pattern.compile("[\"\\\\]");

    private static final Logger logger = LoggerFactory.getLogger(JSONSerializer.class);

    private static boolean nanWarningIssued = false;

    private static String jsonEscape(String input) {
        return JSON_ESCAPE_CHARS.matcher(input).replaceAll("\\$1");
    }

    private static void nanWarn() {
        if (!nanWarningIssued) {
            logger.warn("JSON cannot serialize NaN and infinity values by spec! We'll work-around this by special strings...");
            nanWarningIssued = true;
        }
    }

    @Override
    protected void serializeNull(IConfigNode node, Writer writer, ISerializationContext ctx) throws IOException {
        writer.write("null");
    }

    @Override
    protected String wrapSpecialNumber(Float value) {
        return wrapSpecialNumber(value.toString() + "_f");
    }

    @Override
    protected String wrapSpecialNumber(Double value) {
        return wrapSpecialNumber(value.toString() + "_d");
    }

    protected String wrapSpecialNumber(String numberRep) {
        nanWarn();
        return String.format("__%s", numberRep);
    }

    @Override
    protected void writeBoolean(Boolean value, Writer writer) throws IOException {
        writer.write(value.toString());
    }

    @Override
    protected void writeString(String value, Writer writer) throws IOException {
        writer.write("\"");
        writer.write(jsonEscape(value));
        writer.write("\"");
    }

    @Override
    protected void writeInteger(Integer value, Writer writer) throws IOException {
        writer.write(value.toString());
    }

    @Override
    protected void writeFloat(Float value, Writer writer) throws IOException {
        writer.write(value.toString());
    }

    @Override
    protected void writeDouble(Double value, Writer writer) throws IOException {
        writer.write(value.toString());
    }

    @Override
    protected void enterMap(Writer writer, ISerializationContext ctx) throws IOException {
        writer.write(" {\n");
        super.enterMap(writer, ctx);
    }

    @Override
    protected void walkSerializeMap(Map<String, IConfigNode> map, Writer writer, ISerializationContext ctx) throws IOException {
        Iterator<Map.Entry<String, IConfigNode>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, IConfigNode> entry = iterator.next();

            writer.write(createIndent(ctx.getCurrentIndentLevel()));
            writer.write("\"");
            writer.write(entry.getKey());
            writer.write("\":");
            serializeNode(entry.getValue(), writer, ctx);

            if (iterator.hasNext())
                writer.write(",\n");
        }
    }

    @Override
    protected void leaveMap(Writer writer, ISerializationContext ctx) throws IOException {
        writer.write("\n");
        super.leaveMap(writer, ctx);
        writer.write(createIndent(ctx.getCurrentIndentLevel()));
        writer.write("}");
    }

    @Override
    protected void enterList(Writer writer, ISerializationContext ctx) throws IOException {
        writer.write(" [\n");
        super.enterList(writer, ctx);
    }

    @Override
    protected void walkSerializeList(List<IConfigNode> list, Writer writer, ISerializationContext ctx) throws IOException {
        Iterator<IConfigNode> iterator = list.iterator();
        while (iterator.hasNext()) {
            writer.write(createIndent(ctx.getCurrentIndentLevel()));
            serializeNode(iterator.next(), writer, ctx);

            if (iterator.hasNext())
                writer.write(",\n");
        }
    }

    @Override
    protected void leaveList(Writer writer, ISerializationContext ctx) throws IOException {
        writer.write("\n");
        super.leaveList(writer, ctx);
        writer.write(createIndent(ctx.getCurrentIndentLevel()));
        writer.write("]");
    }

    private String createIndent(Integer indentLevel) {
        StringBuilder builder = new StringBuilder();
        for (Integer i = 0; i < indentLevel; i++) {
            builder.append("  ");
        }
        return builder.toString();
    }

    @Override
    protected boolean isCapableOfSpecialNumbers() {
        return false;
    }
}
