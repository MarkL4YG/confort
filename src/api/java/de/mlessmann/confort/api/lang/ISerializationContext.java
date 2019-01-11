package de.mlessmann.confort.api.lang;

/**
 * The serialization context holds information of a serialization process.
 * This allows serializers to be stateless.
 */
public interface ISerializationContext {

    /**
     * The serialization context tracks the current indentation level.
     * Serializers for formats that support indentation for readability shall take the indentation level into account.
     * @return the current indentation level. As 'x' times one indentation.
     */
    Integer getCurrentIndentLevel();

    /**
     * Increments the tracked indentation level.
     * Serializers for formats that support indentation are supposed to call this
     * when a non-primitive node is serialized.
     */
    void indentIncrement();

    /**
     * Decrements the tracked indentation level.
     * Serializers for formats that support indentation are supposed to call this
     * when a non-primitive node is exited.
     */
    void indentDecrement();
}
