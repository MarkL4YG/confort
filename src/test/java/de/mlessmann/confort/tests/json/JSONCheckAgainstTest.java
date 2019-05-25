package de.mlessmann.confort.tests.json;

import de.mlessmann.confort.LoaderFactory;
import de.mlessmann.confort.api.IConfigNode;
import de.mlessmann.confort.tests.AbstractConfigTest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JSONCheckAgainstTest extends AbstractConfigTest {

    @Override
    protected String getTestResource() {
        return "json/foo.json";
    }

    @Override
    protected String getLoaderIdentification() {
        return "application/json";
    }

    @Test
    public void checkAgainst_OrgJSON() {
        IConfigNode rootNode = loadRoot();
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
