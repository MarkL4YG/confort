package de.mlessmann.confort.tests;

import de.mlessmann.confort.LoaderFactory;
import de.mlessmann.confort.api.lang.IConfigLoader;
import de.mlessmann.confort.lang.RegisterLoaders;
import de.mlessmann.confort.lang.json.JSONConfigLoader;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class BasicRegistryTest {

    protected void ensureRegistered() {
        if (!LoaderFactory.hasAny()) {
            RegisterLoaders.registerLoaders();
        }
    }

    @Test
    public void testGetByShorthand() {
        ensureRegistered();
        assertTrue(LoaderFactory.getLoader("json")
                instanceof JSONConfigLoader);
    }

    @Test
    public void testGetByMime() {
        ensureRegistered();
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
