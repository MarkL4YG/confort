package de.mlessmann.confort;

import de.mlessmann.confort.api.IConfigNode;

import java.util.*;

public class ConfigNode extends ValueHolder implements IConfigNode {

    private Map<String, ConfigNode> map = new TreeMap<>();
    private List<ConfigNode> list = new LinkedList<>();

    @Override
    public boolean isMap() {
        return getValue() == null && list.isEmpty();
    }

    @Override
    public boolean isList() {
        return getValue() == null && !list.isEmpty();
    }

    @Override
    public boolean isPrimitive() {
        return getValue() != null;
    }

    @Override
    public boolean isVirtual() {
        return map.isEmpty() && list.isEmpty() && getValue() == null;
    }

    @Override
    public IConfigNode getNode(String... path) {
        return null;
    }

    @Override
    public List<IConfigNode> asList() {
        return Collections.unmodifiableList(list);
    }

    @Override
    public Map<String, IConfigNode> asMap() {
        return Collections.unmodifiableMap(map);
    }

    public boolean collapse() {
        if (isPrimitive()) {
            return getValue() == null;

        } else if (isList()) {
            list.removeIf(ConfigNode::collapse);
            return list.isEmpty();

        } else if (isMap()) {
            map.entrySet().removeIf(entry -> entry.getValue().collapse());
            return map.isEmpty();
        }

        return true;
    }
}
