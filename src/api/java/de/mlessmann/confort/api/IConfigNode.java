package de.mlessmann.confort.api;

import java.util.List;
import java.util.Map;

/**
 * All configurations are represented by nodes in tree-like structure.
 * A configuration node can hold one of the following states/values
 * * Map of key->configuration node assignments
 * * List of configuration nodes
 * * An {@link IValueHolder}
 */
public interface IConfigNode extends IValueHolder {

    boolean isMap();

    boolean isList();

    boolean isPrimitive();

    boolean isVirtual();

    IConfigNode getNode(String... path);

    List<IConfigNode> asList();

    Map<String, IConfigNode> asMap();
}
