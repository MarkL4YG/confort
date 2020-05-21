package de.mlessmann.confort.tests;

import de.mlessmann.confort.api.except.TypeMismatchException;
import de.mlessmann.confort.node.ConfigNode;
import de.mlessmann.confort.api.IConfigNode;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class BasicNodeTests {

    private static IConfigNode root;

    @BeforeClass
    public static void initRoot() {
        root = new ConfigNode();
    }

    @Test
    public void node_virtualPersists() {
        assertTrue(root.getNode("foo", "bar").isVirtual());
        assertTrue(root.getNode("foo", "bar", "bar").isVirtual());
        assertTrue(root.getNode("foo", "bar").isVirtual());
    }

    @Test
    public void node_nonVirtualPersists() {
        assertTrue(root.getNode("foo", "tee").isVirtual());
        root.getNode("foo", "tee").defaultValue("turtle");
        assertFalse(root.getNode("foo").isVirtual());
        assertFalse(root.getNode("foo", "tee").isVirtual());
        assertEquals("turtle", root.getNode("foo", "tee").asString());
    }

    @Test
    public void node_primitiveValueResets() {
        root.getNode("tee").setString("test");
        assertEquals("test", root.getNode("tee").asString());
        root.getNode("tee").append(new ConfigNode());
        assertEquals("nonpresent", root.getNode("tee").optString().orElse("nonpresent"));
    }

    @Test
    public void node_listValueResets() {
        root.getNode("tee").append(new ConfigNode());
        assertTrue(root.getNode("tee").isList());
        assertFalse(root.getNode("tee").asList().isEmpty());
        root.getNode("tee").setString("reset");
        assertFalse(root.getNode("tee").isList());
        assertTrue(root.getNode("tee").asList().isEmpty());
    }

    @Test
    public void node_mapValueResets() {
        root.getNode("tee").getNode("bla").setString("child");
        assertTrue(root.getNode("tee").isMap());
        assertFalse(root.getNode("tee").asMap().isEmpty());
        root.getNode("tee").setString("reset");
        assertFalse(root.getNode("tee").isMap());
        assertTrue(root.getNode("tee").asMap().isEmpty());
    }

    @Test
    public void node_asValueList() {
        root.getNode("tee").appendValue("a");
        root.getNode("tee").appendValue("b");
        root.getNode("tee").appendValue("c");
        root.getNode("tee").appendValue("d");
        root.getNode("tee").appendValue("e");
        root.getNode("tee").appendValue("f");
        assertTrue(root.getNode("tee").asValueList(String.class)
                .stream()
                .map(Object::getClass)
                .allMatch(String.class::equals));
    }

    @Test(expected = TypeMismatchException.class)
    public void node_asValueList_throws() {
        root.getNode("tee").appendValue("a");
        root.getNode("tee").appendValue("b");
        root.getNode("tee").appendValue("c");
        root.getNode("tee").appendValue("d");
        root.getNode("tee").appendValue("e");
        root.getNode("tee").appendValue("f");
        assertTrue(root.getNode("tee").asValueList(Integer.class)
                .stream()
                .map(Object::getClass)
                .allMatch(String.class::equals));
    }

    @Test
    public void node_asValueMap() {
        root.getNode("tee", "a").setString("a");
        root.getNode("tee", "b").setString("a");
        root.getNode("tee", "c").setString("a");
        root.getNode("tee", "d").setString("a");
        root.getNode("tee", "e").setString("a");
        root.getNode("tee", "f").setString("a");
        assertTrue(root.getNode("tee").asValueMap(String.class)
                .values()
                .stream()
                .map(Object::getClass)
                .allMatch(String.class::equals));
    }

    @Test(expected = TypeMismatchException.class)
    public void node_asValueMap_throws() {
        root.getNode("tee", "a").setString("a");
        root.getNode("tee", "b").setString("a");
        root.getNode("tee", "c").setString("a");
        root.getNode("tee", "d").setString("a");
        root.getNode("tee", "e").setString("a");
        root.getNode("tee", "f").setString("a");
        assertTrue(root.getNode("tee").asValueMap(Integer.class)
                .values()
                .stream()
                .map(Object::getClass)
                .allMatch(String.class::equals));
    }

    @Test
    public void node_removeSingle() {
        root.getNode("tee", "a").setString("a");
        root.getNode("tee", "b").setString("b");
        root.getNode("tee").remove("a");
        assertTrue(root.getNode("tee", "a").isVirtual());
        assertFalse(root.getNode("tee", "b").isVirtual());
    }

    @Test
    public void node_removePath() {
        root.getNode("tee", "a").setString("b");
        root.getNode("tee", "b", "1").setString("b1");
        root.getNode("tee", "b", "2").setString("b2");
        assertNotNull(root.remove("tee", "b", "2"));
        assertTrue(root.getNode("tee", "b", "2").isVirtual());
    }
}
