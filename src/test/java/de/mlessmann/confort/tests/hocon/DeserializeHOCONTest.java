package de.mlessmann.confort.tests.hocon;

import de.mlessmann.confort.tests.AbstractDeserializeTest;

public class DeserializeHOCONTest extends AbstractDeserializeTest {

    @Override
    protected String getTestResource() {
        return "hocon/foo.hocon";
    }

    @Override
    protected String getLoaderIdentification() {
        return "application/hocon";
    }

}
