package de.mlessmann.confort.api;

import java.io.*;

public interface IConfigLoader {

    IConfigNode parse(File file) throws IOException;

    IConfigNode parse(Reader reader) throws IOException;

    void save(IConfigNode root, File file) throws IOException;

    void save(IConfigNode root, Writer writer) throws IOException;
}
