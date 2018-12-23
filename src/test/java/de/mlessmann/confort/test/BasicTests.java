package de.mlessmann.confort.test;

import de.mlessmann.confort.ConfigNode;
import de.mlessmann.confort.api.IConfigNode;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class BasicTests {

    private static IConfigNode root;

    @BeforeClass
    public static void initRoot() {
        root = new ConfigNode();
    }

    @Test
    public void virtualPersists() {
        assertTrue(root.getNode("foo", "bar").isVirtual());
        assertTrue(root.getNode("foo", "bar", "bar").isVirtual());
        assertTrue(root.getNode("foo", "bar").isVirtual());
    }

    @Test
    public void nonVirtualPersists() {
        assertTrue(root.getNode("foo", "tee").isVirtual());
        root.getNode("foo", "tee").defaultValue("turtle");
        assertFalse(root.getNode("foo").isVirtual());
        assertFalse(root.getNode("foo", "tee").isVirtual());
        assertEquals("turtle", root.getNode("foo", "tee").getString());
    }
}
