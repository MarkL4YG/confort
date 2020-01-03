package de.mlessmann.confort.tests.json;

import de.mlessmann.confort.api.lang.IConfigLoader;
import de.mlessmann.confort.lang.json.JSONConfigLoader;
import de.mlessmann.confort.tests.AbstractDeserializeTest;

public class DeserializeJSONTest extends AbstractDeserializeTest {

    @Override
    protected String getTestResource() {
        return "json/foo.json";
    }

    @Override
    protected IConfigLoader getLoader() {
        return new JSONConfigLoader();
    }
}
