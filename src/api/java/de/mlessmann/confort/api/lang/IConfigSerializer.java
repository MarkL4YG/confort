package de.mlessmann.confort.api.lang;

import de.mlessmann.confort.api.IConfigNode;

import java.io.IOException;
import java.io.Writer;

/**
 * Responsible of serializing {@link IConfigNode}s into the own formatting.
 */
public interface IConfigSerializer {

    void serializeNode(IConfigNode node, Writer writer, ISerializationContext ctx) throws IOException;
}
