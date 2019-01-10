package de.mlessmann.confort.config;

import de.mlessmann.confort.api.IConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class FileConfig extends Config {

    private static final Logger logger = LoggerFactory.getLogger(FileConfig.class);

    private File file;

    public FileConfig(IConfigLoader loader, File file) {
        super(loader);
        this.file = file;
    }

    @Override
    protected Writer produceWriter() throws IOException {
        final File directory = file.getAbsoluteFile().getParentFile();
        if (directory == null || (!directory.isDirectory() && !directory.mkdirs())) {
            throw new IOException("Cannot create configuration parent directory: " + directory);
        }
        return new OutputStreamWriter(new FileOutputStream(file), getEncoding());
    }

    @Override
    protected Reader produceReader() throws IOException {
        return new InputStreamReader(new FileInputStream(file), getEncoding());
    }
}
