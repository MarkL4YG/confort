package de.mlessmann.confort.api.lang;

import de.mlessmann.confort.api.IConfigNode;
import de.mlessmann.confort.api.except.ParseException;

import java.io.*;

public interface IConfigLoader {

    IConfigNode parse(File file) throws IOException, ParseException;

    IConfigNode parse(Reader reader) throws IOException, ParseException;

    void save(IConfigNode root, File file) throws IOException;

    void save(IConfigNode root, Writer writer) throws IOException;
}
