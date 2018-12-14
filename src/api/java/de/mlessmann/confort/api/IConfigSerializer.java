package de.mlessmann.confort.api;

import java.io.IOException;
import java.io.Writer;

public interface IConfigSerializer {

    void serializeNode(IConfigNode node, Writer writer) throws IOException;
}
