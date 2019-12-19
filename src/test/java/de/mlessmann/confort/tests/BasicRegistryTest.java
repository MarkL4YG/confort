package de.mlessmann.confort.tests;

import de.mlessmann.confort.LoaderFactory;
import de.mlessmann.confort.api.lang.IConfigLoader;
import de.mlessmann.confort.lang.json.JSONConfigLoader;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class BasicRegistryTest {

    @BeforeClass
    public static void initialize() {
        LoaderFactory.registerLoader(
                JSONConfigLoader.class,
                JSONConfigLoader::new
        );
    }

    @Test
    public void testGetByShorthand() {
        assertTrue(LoaderFactory.getLoader("json")
                instanceof JSONConfigLoader);
    }

    @Test
    public void testGetByMime() {
        assertTrue(LoaderFactory.getLoader("application/json")
                instanceof JSONConfigLoader);
    }

    @Test
    public void testGetByExtendedMime() {
//        assertTrue(LoaderFactory.getLoader("application/json+charset=utf-8")
//                instanceof JSONConfigLoader);
    }

    @Test
    public void testMultiplicity() {
        IConfigLoader first = LoaderFactory.getLoader("json");
        IConfigLoader second = LoaderFactory.getLoader("json");
        assertNotEquals(first, second);
    }
}
