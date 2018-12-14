package de.mlessmann.confort.lang.json;

import de.mlessmann.confort.lang.ConfigSerializer;
import de.mlessmann.confort.api.IConfigNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

public class JSONSerializer extends ConfigSerializer {

    private static final Pattern JSON_ESCAPE_CHARS = Pattern.compile("[\"\\\\]");

    private static final Logger logger = LoggerFactory.getLogger(JSONSerializer.class);

    private static boolean nanWarningIssued = false;

    private static void nanWarn() {
        if (!nanWarningIssued) {
            logger.warn("JSON cannot serialize NaN and infinity values by spec! We'll work-around this by special strings...");
            nanWarningIssued = true;
        }
    }

    @Override
    public void serializePrimitive(IConfigNode node, Writer writer) throws IOException {

        if (node.optBoolean().isPresent()) {
            writer.write(node.optBoolean().get().toString());

        } else if (node.optString().isPresent()) {
            writer.write("\"");
            writer.write(jsonEscape(node.optString().get()));
            writer.write("\"");

        } else if (node.optInteger().isPresent()) {
            writer.write(node.optInteger().get().toString());

        } else if (node.optFloat().isPresent()) {
            Float val = node.optFloat().get();

            if (!val.isInfinite() && !val.isNaN()) {
                writer.write(val.toString());
            } else {
                wrapSpecialNumber(val.toString(), writer);
            }

        } else if (node.optDouble().isPresent()) {
            Double val = node.optDouble().get();

            if (!val.isInfinite() && !val.isNaN()) {
                writer.write(val.toString());
            } else {
                wrapSpecialNumber(val.toString(), writer);
            }
        }
    }

    private String jsonEscape(String input) {
        return JSON_ESCAPE_CHARS.matcher(input).replaceAll("\\$1");
    }

    private void wrapSpecialNumber(String numberRep, Writer writer) throws IOException {
        nanWarn();

        writer.write("\"__");
        writer.write(numberRep);
        writer.write("\"");
    }

    @Override
    public void serializeList(IConfigNode node, Writer writer) throws IOException {
        Iterator<IConfigNode> iterator = node.asList().iterator();
        writer.write("[");

        while (iterator.hasNext()) {
            serializeNode(iterator.next(), writer);

            if (iterator.hasNext())
                writer.write(",");
        }

        writer.write("]");
    }

    @Override
    public void serializeMap(IConfigNode node, Writer writer) throws IOException {
        Iterator<Map.Entry<String, IConfigNode>> iterator = node.asMap().entrySet().iterator();

        writer.write("{");

        while (iterator.hasNext()) {
            Map.Entry<String, IConfigNode> entry = iterator.next();

            writer.write("\"");
            writer.write(entry.getKey());
            writer.write("\":");
            serializeNode(entry.getValue(), writer);

            if (iterator.hasNext())
                writer.write(",");
        }

        writer.write("}");
    }
}
