package de.mlessmann.confort.api;

import java.util.Map;
import java.util.function.BiPredicate;

/**
 * The map-specific api of {@link IConfigNode}s.
 * @implNote At the moment, key-order-stability is not yet guaranteed upon load/save!
 */
public interface IConfigNodeMap {

    /**
     * Returns the node located at the end of the supplied path.
     * This method will always return a value.
     * When the path cannot be walked deeper, the missing rest of the path is created.
     *
     * @apiNote This method is stable! The nodes created in this process will be "persisted" only when a value is written.
     */
    IConfigNode getNode(String... path);

    /**
     * Remove the node that was stored behind the specified key.
     * @return the node that was remove or {@code null}.
     */
    IConfigNode remove(String childName);

    /**
     * Removes all nodes from the mapping, the predicate returns true for.
     */
    void removeIf(BiPredicate<String, IConfigNode> removeCondition);

    /**
     * Adds a new node to this mapping.
     */
    void put(String childName, IConfigNode child);

    /**
     * Returns a {@link Map<String, IConfigNode>} representation of the internal mapping.
     * @implNote the mapping is unmodifiable!
     */
    Map<String, IConfigNode> asMap();
}
