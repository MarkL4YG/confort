package de.mlessmann.confort.tests;

import de.mlessmann.confort.LoaderFactory;
import de.mlessmann.confort.api.IConfigNode;
import de.mlessmann.confort.api.except.ParseException;
import de.mlessmann.confort.api.lang.IConfigLoader;
import de.mlessmann.confort.lang.RegisterLoaders;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.Charset;

public abstract class AbstractConfigTest {
    protected abstract String getTestResource();

    protected abstract IConfigLoader getLoader();

    private InputStream getFooStream() {
        return AbstractDeserializeTest.class.getClassLoader().getResourceAsStream(getTestResource());
    }

    protected IConfigNode loadRoot() {
        RegisterLoaders.registerLoaders();

        try (InputStreamReader reader = new InputStreamReader(getFooStream(), Charset.forName("UTF-8"))) {
            return getLoader().parse(reader, URI.create("resource://" + getTestResource()));

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
