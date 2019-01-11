package de.mlessmann.confort.lang.base.serializers;

import de.mlessmann.confort.api.IConfigNode;
import de.mlessmann.confort.api.lang.ISerializationContext;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

public abstract class AbstractIndentAwareSerializer extends AbstractSerializer {

    @Override
    protected void serializeMap(IConfigNode node, Writer writer, ISerializationContext ctx) throws IOException {
        enterMap(writer, ctx);
        walkSerializeMap(node.asMap(), writer, ctx);
        leaveMap(writer, ctx);
    }

    protected void enterMap(Writer writer, ISerializationContext ctx) throws IOException {
        ctx.indentIncrement();
    }

    protected abstract void walkSerializeMap(Map<String, IConfigNode> map, Writer writer, ISerializationContext ctx) throws IOException;

    protected void leaveMap(Writer writer, ISerializationContext ctx) throws IOException {
        ctx.indentDecrement();
    }

    @Override
    protected void serializeList(IConfigNode node, Writer writer, ISerializationContext ctx) throws IOException {
        enterList(writer, ctx);
        walkSerializeList(node.asList(), writer, ctx);
        leaveList(writer, ctx);
    }

    protected void enterList(Writer writer, ISerializationContext ctx) throws IOException {
        ctx.indentIncrement();
    }

    protected abstract void walkSerializeList(List<IConfigNode> list, Writer writer, ISerializationContext ctx) throws IOException;

    protected void leaveList(Writer writer, ISerializationContext ctx) throws IOException {
        ctx.indentDecrement();
    }
}
