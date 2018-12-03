package de.mlessmann.confort.api;

import java.util.Optional;

/**
 * A grouping interface for any object that can hold/be seen as any of the primitive Java values.
 * The getters do proper type checks:
 * * When the desired return type is assignable. The value will be casted and returned.
 * * When the desired return type is not assignable:
 *   A {@link de.mlessmann.confort.api.except.TypeMismatchException} is thrown for direct getters.
 *   And an empty {@link Optional} is returned for the weak getters.
 * * When the desired return type is not assignable but the classname is the same, a cast will be attempted.
 */
public interface IValueHolder {

    Boolean asBoolean();

    Optional<Boolean> optBoolean();

    String asString();

    Optional<String> optString();

    Integer asInteger();

    Optional<Integer> optInteger();

    Float asFloat();

    Optional<Float> optFloat();

    Double asDouble();

    Optional<Double> optDouble();

    /**
     * Sets the currently stored value to an other one.
     * @param value The new value to be stored.
     *
     * @throws de.mlessmann.confort.api.except.TypeMismatchException
     *         When the given type is not applicable to one of the supported types.
     */
    void setValue(Object value);

    Object getValue();

    <T> T getValue(Class<T> hint);

    <T> Optional<T> optValue(Class<T> hint);
}
