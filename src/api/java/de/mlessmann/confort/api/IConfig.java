package de.mlessmann.confort.api;

import de.mlessmann.confort.api.except.ParseException;

import java.io.IOException;
import java.nio.charset.Charset;

public interface IConfig {

    void setEncoding(Charset encoding);

    void save() throws IOException;

    void load() throws IOException, ParseException;

    void createRoot();

    void setRoot(IConfigNode root);

    IConfigNode getRoot();
}
