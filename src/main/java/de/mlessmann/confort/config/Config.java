package de.mlessmann.confort.config;

import de.mlessmann.confort.api.IConfig;
import de.mlessmann.confort.api.IConfigLoader;
import de.mlessmann.confort.api.IConfigNode;

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
        loader.save(root, produceWriter());
    }

    @Override
    public synchronized void load() throws IOException {
        root = loader.parse(produceReader());
    }

    @Override
    public synchronized IConfigNode getRoot() {
        return root;
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