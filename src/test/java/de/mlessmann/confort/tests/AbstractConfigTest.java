package de.mlessmann.confort.tests;

import de.mlessmann.confort.api.IConfigNode;
import de.mlessmann.confort.api.except.ParseException;
import de.mlessmann.confort.api.lang.IConfigLoader;
import de.mlessmann.confort.lang.RegisterLoaders;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public abstract class AbstractConfigTest {

    static {
        RegisterLoaders.registerLoaders();
    }

    protected abstract String getTestResource();

    protected abstract IConfigLoader getLoader();

    private InputStream getFooStream() {
        return AbstractDeserializeTest.class.getClassLoader().getResourceAsStream(getTestResource());
    }

    protected IConfigNode loadRoot() {
        try (InputStreamReader reader = new InputStreamReader(getFooStream(), StandardCharsets.UTF_8)) {
            return getLoader().parse(reader, URI.create("resource://" + getTestResource()));

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
