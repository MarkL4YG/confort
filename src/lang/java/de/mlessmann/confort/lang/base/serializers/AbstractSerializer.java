package de.mlessmann.confort.lang.base.serializers;

import de.mlessmann.confort.api.IConfigNode;
import de.mlessmann.confort.api.lang.IConfigSerializer;
import de.mlessmann.confort.api.lang.ISerializationContext;

import java.io.IOException;
import java.io.Writer;

public abstract class AbstractSerializer implements IConfigSerializer {

    @Override
    public void serializeNode(IConfigNode node, Writer writer, ISerializationContext ctx) throws IOException {

        if (node.isPrimitive()) {
            serializePrimitive(node, writer, ctx);
        } else if (node.isMap()) {
            serializeMap(node, writer, ctx);
        } else if (node.isList()) {
            serializeList(node, writer, ctx);
        } else {
            throw new IllegalArgumentException("Cannot serialize node: unknown state!");
        }
    }

    protected void serializePrimitive(IConfigNode node, Writer writer, ISerializationContext ctx) throws IOException {

        if (node.optBoolean().isPresent()) {
            writeBoolean(node.asBoolean(), writer);

        } else if (node.optString().isPresent()) {
            writeString(node.asString(), writer);

        } else if (node.optInteger().isPresent()) {
            writeInteger(node.asInteger(), writer);

        } else if (node.optFloat().isPresent()) {
            Float val = node.asFloat();

            if (isCapableOfSpecialNumbers() || (!val.isInfinite() && !val.isNaN())) {
                writeFloat(val, writer);
            } else {
                writeString(wrapSpecialNumber(val), writer);
            }

        } else if (node.optDouble().isPresent()) {
            Double val = node.asDouble();

            if (isCapableOfSpecialNumbers() || (!val.isInfinite() && !val.isNaN())) {
                writeDouble(val, writer);
            } else {
                writeString(wrapSpecialNumber(val), writer);
            }
        }
    }

    protected abstract void serializeMap(IConfigNode node, Writer writer, ISerializationContext ctx) throws IOException;

    protected abstract void serializeList(IConfigNode node, Writer writer, ISerializationContext ctx) throws IOException;

    protected abstract void writeBoolean(Boolean value, Writer writer) throws IOException;

    protected abstract void writeString(String value, Writer writer) throws IOException;

    protected abstract void writeInteger(Integer value, Writer writer) throws IOException;

    protected abstract void writeFloat(Float value, Writer writer) throws IOException;

    protected abstract void writeDouble(Double value, Writer writer) throws IOException;

    protected abstract String wrapSpecialNumber(Float value);

    protected abstract String wrapSpecialNumber(Double value);

    protected boolean isCapableOfSpecialNumbers() {
        return true;
    }
}
