package de.mlessmann.confort.api;

import java.math.BigInteger;
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

    Boolean getBoolean();

    void setBoolean(Boolean value);

    String asString();

    Optional<String> optString();

    String getString();

    void setString(String value);

    Integer asInteger();

    Optional<Integer> optInteger();

    Integer getInteger();

    void setInteger(Integer value);

    Float asFloat();

    Optional<Float> optFloat();

    Float getFloat();

    void setFloat(Float value);

    Double asDouble();

    Optional<Double> optDouble();

    Double getDouble();

    void setDouble(Double value);

    Object getValue();

    <T> T getValue(Class<T> hint);

    <T> Optional<T> optValue(Class<T> hint);

    <T> boolean defaultValue(T value);
}
