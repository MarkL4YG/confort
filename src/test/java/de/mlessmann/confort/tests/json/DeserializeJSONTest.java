package de.mlessmann.confort.tests.json;

import de.mlessmann.confort.tests.AbstractDeserializeTest;

public class DeserializeJSONTest extends AbstractDeserializeTest {

    @Override
    protected String getTestResource() {
        return "json/foo.json";
    }

    @Override
    protected String getLoaderIdentification() {
        return "application/json";
    }
}
