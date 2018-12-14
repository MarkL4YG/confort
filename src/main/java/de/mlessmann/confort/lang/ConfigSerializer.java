package de.mlessmann.confort.lang;

import de.mlessmann.confort.api.IConfigNode;
import de.mlessmann.confort.api.IConfigSerializer;

import java.io.IOException;
import java.io.Writer;

public abstract class ConfigSerializer implements IConfigSerializer {

    @Override
    public void serializeNode(IConfigNode node, Writer writer) throws IOException {
        if (!node.isVirtual()) {
            if (node.isPrimitive()) {
                serializePrimitive(node, writer);

            } else if (node.isList()) {
                serializeList(node, writer);

            } else if (node.isMap()) {
                serializeMap(node, writer);

            } else {
                throw new IllegalArgumentException("Cannot serialize node: unknown state!");
            }
        }
    }

    public abstract void serializePrimitive(IConfigNode node, Writer writer) throws IOException;

    public abstract void serializeList(IConfigNode node, Writer writer) throws IOException;

    public abstract void serializeMap(IConfigNode node, Writer writer) throws IOException;
}
