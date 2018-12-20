package de.mlessmann.confort.config;

import de.mlessmann.confort.api.IConfigLoader;

import java.io.*;

public class FileConfig extends Config {

    private File file;

    public FileConfig(IConfigLoader loader, File file) {
        super(loader);
        this.file = file;
    }

    @Override
    protected Writer produceWriter() throws IOException {
        return new OutputStreamWriter(new FileOutputStream(file), getEncoding());
    }

    @Override
    protected Reader produceReader() throws IOException {
        return new InputStreamReader(new FileInputStream(file), getEncoding());
    }
}
