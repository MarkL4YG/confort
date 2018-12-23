package de.mlessmann.confort.config;

import de.mlessmann.confort.ConfigNode;
import de.mlessmann.confort.api.IConfig;
import de.mlessmann.confort.api.IConfigLoader;
import de.mlessmann.confort.api.IConfigNode;
import de.mlessmann.confort.api.except.ParseException;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

public abstract class Config implements IConfig {

    private static final Charset INITIAL_CHARSET = Charset.forName("UTF-8");

    private Charset encoding = INITIAL_CHARSET;
    private IConfigNode root;
    private IConfigLoader loader;

    public Config(IConfigLoader loader) {
        this.loader = loader;
    }

    @Override
    public synchronized void save() throws IOException {
        if (root != null) {
            try (Writer writer = produceWriter()) {
                loader.save(root, writer);
            }
        }
    }

    @Override
    public synchronized void load() throws IOException, ParseException {
        try (Reader reader = produceReader()) {
            root = loader.parse(reader);
        }
    }

    @Override
    public synchronized void createRoot() {
        root = new ConfigNode();
    }

    @Override
    public synchronized IConfigNode getRoot() {
        return root;
    }

    @Override
    public synchronized void setRoot(IConfigNode root) {
        this.root = root;
    }

    protected abstract Writer produceWriter() throws IOException;

    protected abstract Reader produceReader() throws IOException;

    public Charset getEncoding() {
        return encoding;
    }

    @Override
    public void setEncoding(Charset encoding) {
        this.encoding = encoding;
    }
}
