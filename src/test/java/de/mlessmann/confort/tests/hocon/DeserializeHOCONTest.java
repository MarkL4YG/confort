package de.mlessmann.confort.tests.hocon;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import de.mlessmann.confort.LoaderFactory;
import de.mlessmann.confort.api.IConfigNode;
import de.mlessmann.confort.api.except.ParseException;
import de.mlessmann.confort.lang.RegisterLoaders;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class DeserializeHOCONTest {

    private static IConfigNode rootNode;

    private static InputStream getFooStream() {
        return DeserializeHOCONTest.class.getClassLoader().getResourceAsStream("hocon/foo.hocon");
    }

    @BeforeClass
    public static void setup() {
        RegisterLoaders.registerLoaders();

        try (InputStreamReader reader = new InputStreamReader(getFooStream(), Charset.forName("UTF-8"))) {
            rootNode = LoaderFactory.getLoader("hocon").parse(reader);

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test_read_GeneralDataTypes() {
        assertFalse(rootNode.getNode("foo").isVirtual());
        assertTrue(rootNode.getNode("foo").isMap());
        assertTrue(rootNode.getNode("foo", "bar").isList());
        assertTrue(rootNode.getNode("foo", "tee").isPrimitive());
    }

    @Test
    public void test_read_SpecialFloats() {
        assertTrue(rootNode.getNode("nan").optFloat().get().isNaN());
        assertEquals(Float.POSITIVE_INFINITY, rootNode.getNode("inf").optFloat().get(), 0.0);
        assertEquals(Float.NEGATIVE_INFINITY, rootNode.getNode("Ninf").optFloat().get(), 0.0);

        assertTrue(rootNode.getNode("nanD").optDouble().get().isNaN());
        assertEquals(Double.POSITIVE_INFINITY, rootNode.getNode("infD").optDouble().get(), 0.0);
        assertEquals(Double.NEGATIVE_INFINITY, rootNode.getNode("NinfD").optDouble().get(), 0.0);
    }

    @Test
    public void test_escaped_Strings() {
        assertEquals("\n\n\\n", rootNode.getNode("escaped")
                .optString().orElseThrow(() -> new IllegalStateException("Escaped str not set to string!")));
        assertEquals("4", rootNode.getNode("unicode")
                .optString().orElseThrow(() -> new IllegalStateException("Escaped unicode str not set to string!")));
        assertEquals("Hello\"World\"", rootNode.getNode("escaped_quote")
                .optString().orElseThrow(() -> new IllegalStateException("Escaped quote not set to string.")));
    }

    @Test
    public void test_write_Save() {
        try {
            StringWriter testWriter = new StringWriter();
            LoaderFactory.getLoader("json").save(rootNode, testWriter);
            String savedText = testWriter.toString();
            savedText = savedText.replaceAll("\\s", "");

            Config deserialized = ConfigFactory.load(savedText);
            assertTrue(deepHOCONEquals(rootNode, deserialized));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean deepHOCONEquals(IConfigNode confort, Object hocon) {
        if (confort.isMap() && hocon instanceof Config) {
            Config object = (Config) hocon;
            Map<String, IConfigNode> confortMap = confort.asMap();
            if (object.entrySet().size() != confortMap.size()) {
                return false;
            }

            for (Map.Entry<String, IConfigNode> entry : confortMap.entrySet()) {
                if (object.atKey(entry.getKey()) == null) {
                    return false;
                }

                if (!deepHOCONEquals(entry.getValue(), object.atKey(entry.getKey()))) {
                    return false;
                }
            }

        } else if (confort.isList() && hocon instanceof List) {
            List<IConfigNode> list = confort.asList();
            List lst = ((List) hocon);
            if (list.size() != lst.size()) {
                return false;
            }

            for (int i = 0; i < list.size(); i++) {
                if (!deepHOCONEquals(list.get(i), lst.get(i))) {
                    return false;
                }
            }

        } else if (confort.isPrimitive()) {

            if ((confort.optFloat().isPresent() && confort.optFloat().get().isNaN())
                    || (confort.optFloat().isPresent() && confort.optFloat().get().isInfinite())
                    || (confort.optDouble().isPresent() && confort.optDouble().get().isNaN())
                    || (confort.optDouble().isPresent() && confort.optDouble().get().isInfinite())) {
                return true;

            } else {
                assertEquals(confort.getValue(), hocon);
            }
        }

        return true;
    }
}
