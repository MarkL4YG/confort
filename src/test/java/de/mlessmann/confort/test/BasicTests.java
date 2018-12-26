package de.mlessmann.confort.test;

import de.mlessmann.confort.node.ConfigNode;
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
        assertEquals("turtle", root.getNode("foo", "tee").asString());
    }

    @Test
    public void primitiveValueResets() {
        root.getNode("tee").setString("test");
        assertEquals("test", root.getNode("tee").asString());
        root.getNode("tee").append(new ConfigNode());
        assertEquals("nonpresent", root.getNode("tee").optString().orElse("nonpresent"));
    }

    @Test
    public void listValueResets() {
        root.getNode("tee").append(new ConfigNode());
        assertTrue(root.getNode("tee").isList());
        assertFalse(root.getNode("tee").asList().isEmpty());
        root.getNode("tee").setString("reset");
        assertFalse(root.getNode("tee").isList());
        assertTrue(root.getNode("tee").asList().isEmpty());
    }

    @Test
    public void mapValueResets() {
        root.getNode("tee").getNode("bla").setString("child");
        assertTrue(root.getNode("tee").isMap());
        assertFalse(root.getNode("tee").asMap().isEmpty());
        root.getNode("tee").setString("reset");
        assertFalse(root.getNode("tee").isMap());
        assertTrue(root.getNode("tee").asMap().isEmpty());
    }
}
