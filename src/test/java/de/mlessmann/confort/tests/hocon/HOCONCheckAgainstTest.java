package de.mlessmann.confort.tests.hocon;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import de.mlessmann.confort.LoaderFactory;
import de.mlessmann.confort.api.IConfigNode;
import de.mlessmann.confort.api.lang.IConfigLoader;
import de.mlessmann.confort.lang.hocon.HOCONConfigLoader;
import de.mlessmann.confort.tests.AbstractConfigTest;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HOCONCheckAgainstTest extends AbstractConfigTest {

    @Override
    protected String getTestResource() {
        return "hocon/foo.hocon";
    }

    @Override
    protected IConfigLoader getLoader() {
        return new HOCONConfigLoader();
    }

    @Test
    public void checkAgainst_Typesafe() {
        IConfigNode rootNode = loadRoot();
        try {
            StringWriter testWriter = new StringWriter();
            LoaderFactory.getLoader("json").save(rootNode, testWriter);
            String savedText = testWriter.toString();
            savedText = savedText.replaceAll("\\s", "");

            Config deserialized = ConfigFactory.parseString(savedText);
            assertTrue(deepHOCONEquals(rootNode, deserialized));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean deepHOCONEquals(IConfigNode confort, Object hocon) {
        if (confort.isMap() && hocon instanceof Map) {
            Map object = (Map) hocon;
            Map<String, IConfigNode> confortMap = confort.asMap();
            if (object.size() != confortMap.size()) {
                return false;
            }

            for (Map.Entry<String, IConfigNode> entry : confortMap.entrySet()) {
                if (object.getOrDefault(entry.getKey(), null) == null) {
                    return false;
                }

                if (!deepHOCONEquals(entry.getValue(), object.getOrDefault(entry.getKey(), null))) {
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
