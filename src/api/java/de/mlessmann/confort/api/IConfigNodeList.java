package de.mlessmann.confort.api;

import java.util.List;
import java.util.function.Predicate;

/**
 * The list-specific api of {@link IConfigNode}s.
 *
 * @implNote At the moment, list-stability is not yet guaranteed upon load/save!
 */
public interface IConfigNodeList {

    /**
     * Append a node to this list.
     */
    void append(IConfigNode child);

    /**
     * Append a value to this list. Automatically wraps the value into a node.
     */
    void appendValue(Object value);

    /**
     * Prepend a node to this list.
     */
    void prepend(IConfigNode child);

    /**
     * Prepend a value to this list. Automatically wraps the value into a node.
     */
    void prependValue(Object value);

    /**
     * Remove a node based on its index.
     */
    IConfigNode remove(Integer index);

    /**
     * Removes all nodes from the list, the predicate returns true for.
     */
    void removeIf(Predicate<IConfigNode> removeCondition);

    /**
     * Removes all nodes from the list.
     * @implNote Please be aware of the overloading with {@link IConfigNodeMap#clear()} as this will clear both types of nodes regardless of what the type is.
     */
    void clear();

    /**
     * Returns a {@link List<IConfigNode>} representation of the internal list.
     *
     * @implNote the list is unmodifiable!
     */
    List<IConfigNode> asList();
}
