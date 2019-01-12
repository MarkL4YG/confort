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
     * Prepend a node to this list.
     */
    void prepend(IConfigNode child);

    /**
     * Remove a node based on its index.
     */
    IConfigNode remove(Integer index);

    /**
     * Removes all nodes from the list, the predicate returns true for.
     */
    void removeIf(Predicate<IConfigNode> removeCondition);

    /**
     * Returns a {@link List<IConfigNode>} representation of the internal list.
     *
     * @implNote the list is unmodifiable!
     */
    List<IConfigNode> asList();
}
