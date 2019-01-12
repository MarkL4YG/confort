package de.mlessmann.confort.api.lang;

import de.mlessmann.confort.api.IConfig;
import de.mlessmann.confort.api.IConfigNode;
import de.mlessmann.confort.api.except.ParseException;

import java.io.*;

/**
 * Responsible for running its dedicated parsing algorithm over the input.
 */
public interface IConfigLoader {

    /**
     * Convenience method for the most common case.
     * @see #parse(Reader)
     */
    IConfigNode parse(File file) throws IOException, ParseException;

    /**
     * @see IConfig#load()
     */
    IConfigNode parse(Reader reader) throws IOException, ParseException;

    /**
     * Convenience method for the most common case.
     * @see #save(IConfigNode, Writer)
     */
    void save(IConfigNode root, File file) throws IOException;

    /**
     * @see IConfig#save()
     */
    void save(IConfigNode root, Writer writer) throws IOException;
}
