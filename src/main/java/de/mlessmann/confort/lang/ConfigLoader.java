package de.mlessmann.confort.lang;

import de.mlessmann.confort.api.IConfigLoader;
import de.mlessmann.confort.api.IConfigNode;

import java.io.*;

public abstract class ConfigLoader implements IConfigLoader {

    public IConfigNode parse(File configFile) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(configFile))) {
            return parse(reader);
        }
    }

    public abstract IConfigNode parse(Reader reader) throws IOException;

    public void save(IConfigNode root, File configFile) throws IOException {
        try (OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(configFile))) {
            save(root, out);
        }
    }

    public void save(IConfigNode root, Writer writer) throws IOException {
        root.collapse();
        getSerializer().serializeNode(root, writer);
    }

    public abstract ConfigSerializer getSerializer();
}
