package de.mlessmann.confort;

import de.mlessmann.confort.api.IValueHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.NoSuchElementException;
import java.util.Optional;

public class ValueHolder implements IValueHolder {

    private static Logger logger = LoggerFactory.getLogger(ValueHolder.class);

    private Object value;

    @Override
    public Boolean asBoolean() {
        return optBoolean().orElseThrow(() -> new NoSuchElementException("Not set to a Boolean!"));
    }

    @Override
    public Optional<Boolean> optBoolean() {
        return optValue(Boolean.class);
    }

    @Override
    public Boolean optBoolean(Boolean def) {
        return optBoolean().orElse(def);
    }

    @Override
    public void setBoolean(Boolean value) {
        setValue(value);
    }

    @Override
    public String asString() {
        return optString().orElseThrow(() -> new NoSuchElementException("Not set to a String!"));
    }

    @Override
    public Optional<String> optString() {
        return optValue(String.class);
    }

    @Override
    public String optString(String def) {
        return optString().orElse(def);
    }

    @Override
    public void setString(String value) {
        setValue(value);
    }

    @Override
    public Integer asInteger() {
        return optInteger().orElseThrow(() -> new NoSuchElementException("Not set to an Integer!"));
    }

    @Override
    public Optional<Integer> optInteger() {
        return optValue(Integer.class);
    }

    @Override
    public Integer optInteger(Integer def) {
        return optInteger().orElse(def);
    }

    @Override
    public void setInteger(Integer value) {
        setValue(value);
    }

    @Override
    public Float asFloat() {
        return optFloat().orElseThrow(() -> new NoSuchElementException("Not set to a Float!"));
    }

    @Override
    public Optional<Float> optFloat() {
        return optValue(Float.class);
    }

    @Override
    public Float optFloat(Float def) {
        return optFloat().orElse(def);
    }

    @Override
    public void setFloat(Float value) {
        setValue(value);
    }

    @Override
    public Double asDouble() {
        return optDouble().orElseThrow(() -> new NoSuchElementException("Not set to a Double!"));
    }

    @Override
    public Optional<Double> optDouble() {
        return optValue(Double.class);
    }

    @Override
    public Double optDouble(Double def) {
        return optDouble().orElse(def);
    }

    @Override
    public void setDouble(Double value) {
        setValue(value);
    }

    protected synchronized void setValue(Object value) {
        this.value = value;
    }

    @Override
    public synchronized Object getValue() {
        return value;
    }

    @Override
    @SuppressWarnings("squid:S1872")
    public <T> T getValue(Class<T> hint) {
        Object currentVal = getValue();

        if (currentVal != null) {
            if (hint.isAssignableFrom(currentVal.getClass())) {
                return hint.cast(currentVal);

            } else if (hint.getName().equals(currentVal.getClass().getName())) {
                logger.warn("Incompatible types share same name! Different class loaders? {}", hint.getName());
            }
        }

        return null;
    }

    @Override
    public <T> Optional<T> optValue(Class<T> hint) {
        return Optional.ofNullable(getValue(hint));
    }

    @Override
    public synchronized <T> boolean defaultValue(T value) {
        if (getValue() == null) {
            setValue(value);
            return true;
        }
        return false;
    }
}
