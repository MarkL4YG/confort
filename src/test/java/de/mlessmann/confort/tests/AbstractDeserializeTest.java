package de.mlessmann.confort.tests;

import de.mlessmann.confort.LoaderFactory;
import de.mlessmann.confort.api.IConfigNode;
import de.mlessmann.confort.api.except.ParseException;
import de.mlessmann.confort.lang.RegisterLoaders;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import static org.junit.Assert.*;

public abstract class AbstractDeserializeTest extends AbstractConfigTest {

    private IConfigNode rootNode = loadRoot();

    @Test
    public void deserialize_GeneralDataTypes() {
        assertFalse(rootNode.getNode("foo").isVirtual());
        assertTrue(rootNode.getNode("foo").isMap());
        assertTrue(rootNode.getNode("foo", "bar").isList());
        assertTrue(rootNode.getNode("foo", "tee").isPrimitive());
    }

    @Test
    public void deserialize_SpecialFloats() {
        assertTrue(rootNode.getNode("nan").optFloat().get().isNaN());
        assertEquals(Float.POSITIVE_INFINITY, rootNode.getNode("inf").optFloat().get(), 0.0);
        assertEquals(Float.NEGATIVE_INFINITY, rootNode.getNode("Ninf").optFloat().get(), 0.0);

        assertTrue(rootNode.getNode("nanD").optDouble().get().isNaN());
        assertEquals(Double.POSITIVE_INFINITY, rootNode.getNode("infD").optDouble().get(), 0.0);
        assertEquals(Double.NEGATIVE_INFINITY, rootNode.getNode("NinfD").optDouble().get(), 0.0);
    }

    @Test
    public void deserialize_EscapedStrings() {
        assertEquals("\n\n\\n", rootNode.getNode("escaped")
                .optString().orElseThrow(() -> new IllegalStateException("Escaped str not set to string!")));
        assertEquals("4", rootNode.getNode("unicode")
                .optString().orElseThrow(() -> new IllegalStateException("Escaped unicode str not set to string!")));
        assertEquals("Hello\"World\"", rootNode.getNode("escaped_quote")
                .optString().orElseThrow(() -> new IllegalStateException("Escaped quote not set to string.")));
    }
}
