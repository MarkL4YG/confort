package de.mlessmann.confort.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * All configurations are represented by nodes in tree-like structure.
 * A configuration node can hold one of the following states/values
 * * Map of key->configuration node assignments
 * * List of configuration nodes
 * * An {@link IValueHolder}
 *
 * @apiNote This interface is split into multiple interfaces for readability.
 */
public interface IConfigNode extends IConfigNodePrimitive, IConfigNodeList, IConfigNodeMap {

    /**
     * Whether or not a non-virtual mapping is contained in this node.
     */
    boolean isMap();

    /**
     * Optionally returns the mapping based on {@link #isMap()}
     *
     * @return The mapping if {@link #isMap()} returns true. An empty {@link Optional} otherwise.
     * @implNote The mapping is unmodifiable.
     */
    Optional<Map<String, IConfigNode>> optMap();

    /**
     * Whether or not a non-empty list is contained in this node.
     */
    boolean isList();

    /**
     * Optionally returns the list based on {@link #isMap()}
     *
     * @return The list if {@link #isList()} returns true. An empty {@link Optional} otherwise.
     * @implNote The list is unmodifiable.
     */
    Optional<List<IConfigNode>> optList();

    /**
     * Whether or not a primitive value is held in this node.
     *
     * @see IValueHolder
     */
    boolean isPrimitive();

    /**
     * Whether or not this node (or any of its transitive children) contain values.
     */
    boolean isVirtual();

    /**
     * Tells nodes to drop all virtual members (as defined by {@link #isVirtual()}).
     * Returns whether or not this node may be dropped too.
     */
    boolean collapse();

    /**
     * Explicitly sets this node to hold a {@code null} value.
     * This will explicitly disable {@link #collapse()} for this node.
     * Note that this behavior is different from calling a setter with {@code null} as the parameter value.
     */
    void setNull();

    /**
     * Explicitly sets this node to hold a {@code List} value.
     * This will explicitly disable {@link #collapse()} for this node.
     */
    void setList();

    /**
     * Explicitly sets this node to hold a {@code Map} value.
     * This will explicitly disable {@link #collapse()} for this node.
     */
    void setMap();

    /**
     * Return a new root node of the same implementation.
     * This exists to avoid users of the library having to know the node implementation.
     */
    IConfigNode createNewInstance();
}
