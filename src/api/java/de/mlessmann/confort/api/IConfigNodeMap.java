package de.mlessmann.confort.api;

import java.util.Map;

public interface IConfigNodeMap {

    IConfigNode getNode(String... path);

    IConfigNode remove(String childName);

    void put(String childName, IConfigNode child);

    Map<String, IConfigNode> asMap();
}
