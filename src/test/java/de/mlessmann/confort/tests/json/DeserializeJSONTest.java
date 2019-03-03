package de.mlessmann.confort.tests.json;

import de.mlessmann.confort.LoaderFactory;
import de.mlessmann.confort.api.IConfigNode;
import de.mlessmann.confort.api.except.ParseException;
import de.mlessmann.confort.lang.RegisterLoaders;
import org.json.JSONArray;
import org.json.JSONObject;
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

public class DeserializeJSONTest {

    private static IConfigNode rootNode;

    private static InputStream getFooStream() {
        return DeserializeJSONTest.class.getClassLoader().getResourceAsStream("json/foo.json");
    }

    @BeforeClass
    public static void setup() {
        RegisterLoaders.registerLoaders();

        try (InputStreamReader reader = new InputStreamReader(getFooStream(), Charset.forName("UTF-8"))) {
            rootNode = LoaderFactory.getLoader("json").parse(reader);

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
        assertEquals("\n\n\\n", rootNode.getNode("escaped").asString());
        assertEquals("4", rootNode.getNode("unicode").asString());
        assertEquals("Hello\"World\"", rootNode.getNode("escaped_quote").asString());
    }

    @Test
    public void test_write_Save() {
        try {
            StringWriter testWriter = new StringWriter();
            LoaderFactory.getLoader("json").save(rootNode, testWriter);
            String savedText = testWriter.toString();
            savedText = savedText.replaceAll("\\s", "");

            JSONObject deserialized = new JSONObject(savedText);
            assertTrue(deepJSONEquals(rootNode, deserialized));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean deepJSONEquals(IConfigNode confort, Object json) {
        if (confort.isMap() && json instanceof JSONObject) {
            JSONObject object = (JSONObject) json;
            Map<String, IConfigNode> confortMap = confort.asMap();
            if (object.length() != confortMap.size())
                return false;

            for (Map.Entry<String, IConfigNode> entry : confortMap.entrySet()) {
                if (!object.has(entry.getKey())) {
                    return false;
                }

                if (!deepJSONEquals(entry.getValue(), object.get(entry.getKey())))
                    return false;
            }

        } else if (confort.isList() && json instanceof JSONArray) {
            List<IConfigNode> list = confort.asList();
            JSONArray arr = ((JSONArray) json);
            if (list.size() != arr.length())
                return false;

            for (int i = 0; i < list.size(); i++) {
                if (!deepJSONEquals(list.get(i), arr.get(i)))
                    return false;
            }
        } else if (confort.isPrimitive()) {

            if ((confort.optFloat().isPresent() && confort.optFloat().get().isNaN())
                || (confort.optFloat().isPresent() && confort.optFloat().get().isInfinite())
                || (confort.optDouble().isPresent() && confort.optDouble().get().isNaN())
                || (confort.optDouble().isPresent() && confort.optDouble().get().isInfinite())) {
                return true;

            } else {
                assertEquals(confort.getValue(), json);
            }
        }

        return true;
    }
}
