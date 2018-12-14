package de.mlessmann.confort;

import de.mlessmann.confort.api.IConfigNode;

import java.util.*;

public class ConfigNode extends ValueHolder implements IConfigNode {

    private Map<String, IConfigNode> map = new TreeMap<>();
    private List<IConfigNode> list = new LinkedList<>();

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
        if (path.length <= 0) {
            return this;

        } else if (path.length == 1) {
            return map.getOrDefault(path[0], new ConfigNode());

        } else {
            IConfigNode currentTarget = this;
            for (String s : path) {
                currentTarget = currentTarget.getNode(s);
            }
            return currentTarget;
        }
    }

    @Override
    public List<IConfigNode> asList() {
        return Collections.unmodifiableList(list);
    }

    @Override
    public synchronized IConfigNode remove(Integer index) {
        return list.remove(0);
    }

    @Override
    public synchronized void prepend(IConfigNode child) {
        list.add(0, child);
    }

    @Override
    public synchronized void append(IConfigNode child) {
        list.add(child);
    }

    @Override
    public Map<String, IConfigNode> asMap() {
        return Collections.unmodifiableMap(map);
    }

    @Override
    public synchronized void put(String childName, IConfigNode child) {
        map.put(childName, child);
    }

    @Override
    public synchronized IConfigNode remove(String childName) {
        return map.remove(childName);
    }

    @Override
    public boolean collapse() {
        if (isList()) {
            list.removeIf(IConfigNode::collapse);

        } else if (isMap()) {
            map.entrySet().removeIf(entry -> entry.getValue().collapse());
        }

        return isVirtual();
    }
}
