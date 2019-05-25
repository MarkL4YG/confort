package de.mlessmann.confort.tests.json;

import de.mlessmann.confort.LoaderFactory;
import de.mlessmann.confort.api.IConfigNode;
import de.mlessmann.confort.api.except.ParseException;
import de.mlessmann.confort.lang.RegisterLoaders;
import de.mlessmann.confort.tests.AbstractDeserializeTest;
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
