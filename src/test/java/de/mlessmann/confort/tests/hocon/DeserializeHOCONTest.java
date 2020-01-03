package de.mlessmann.confort.tests.hocon;

import de.mlessmann.confort.api.lang.IConfigLoader;
import de.mlessmann.confort.lang.hocon.HOCONConfigLoader;
import de.mlessmann.confort.tests.AbstractDeserializeTest;

public class DeserializeHOCONTest extends AbstractDeserializeTest {

    @Override
    protected String getTestResource() {
        return "hocon/foo.hocon";
    }

    @Override
    protected IConfigLoader getLoader() {
        return new HOCONConfigLoader();
    }

}
