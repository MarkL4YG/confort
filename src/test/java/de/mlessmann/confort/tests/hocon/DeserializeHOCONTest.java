package de.mlessmann.confort.tests.hocon;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import de.mlessmann.confort.LoaderFactory;
import de.mlessmann.confort.api.IConfigNode;
import de.mlessmann.confort.api.except.ParseException;
import de.mlessmann.confort.lang.RegisterLoaders;
import de.mlessmann.confort.tests.AbstractDeserializeTest;
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
