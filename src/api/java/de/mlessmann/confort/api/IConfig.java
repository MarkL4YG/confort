package de.mlessmann.confort.api;

import java.io.IOException;
import java.nio.charset.Charset;

public interface IConfig {

    void setEncoding(Charset encoding);

    void save() throws IOException;

    void load() throws IOException;

    IConfigNode getRoot();
}
