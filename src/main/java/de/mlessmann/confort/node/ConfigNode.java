package de.mlessmann.confort.node;

import de.mlessmann.confort.ValueHolder;
import de.mlessmann.confort.api.IConfigNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class ConfigNode extends ValueHolder implements IConfigNode {

    private static final Logger logger = LoggerFactory.getLogger(ConfigNode.class);

    private ConfigNode parent;
    private Map<String, IConfigNode> map = new TreeMap<>();
    private List<IConfigNode> list = new LinkedList<>();
    private TrackingMode trackingMode = TrackingMode.VIRTUAL;

    public ConfigNode() {
        parent = null;
    }

    public ConfigNode(ConfigNode parent) {
        this();
        attachParent(parent);
    }

    @Override
    public synchronized boolean isMap() {
        return trackingMode == TrackingMode.MAP;
    }

    @Override
    public synchronized Optional<Map<String, IConfigNode>> optMap() {
        return Optional.ofNullable(isMap() ? asMap() : null);
    }

    @Override
    public synchronized boolean isList() {
        return trackingMode == TrackingMode.LIST;
    }

    @Override
    public synchronized Optional<List<IConfigNode>> optList() {
        return Optional.ofNullable(isList() ? asList() : null);
    }

    @Override
    public synchronized boolean isPrimitive() {
        return trackingMode == TrackingMode.PRIMITIVE;
    }

    @Override
    public synchronized boolean isVirtual() {
        if (trackingMode == TrackingMode.VIRTUAL) {
            return true;

        } else if (trackingMode == TrackingMode.LIST) {
            return list.isEmpty() || list.stream().allMatch(IConfigNode::isVirtual);

        } else if (trackingMode == TrackingMode.MAP) {
            return map.isEmpty() || map.values().stream().allMatch(IConfigNode::isVirtual);

        } else if (trackingMode == TrackingMode.PRIMITIVE) {
            return getValue() == null;
        } else {
            logger.error("Unaccounted tracking mode! {}", trackingMode);
            return true;
        }
    }

    @Override
    public synchronized IConfigNode getNode(String... path) {
        if (path.length <= 0) {
            return this;

        } else if (path.length == 1) {
            return map.computeIfAbsent(path[0], key -> new ConfigNode().attachParent(this));

        } else {
            IConfigNode currentTarget = this;
            for (String s : path) {
                currentTarget = currentTarget.getNode(s);
            }
            return currentTarget;
        }
    }

    @Override
    public synchronized List<IConfigNode> asList() {
        return Collections.unmodifiableList(list);
    }

    @Override
    public synchronized IConfigNode remove(Integer index) {
        return list.remove(0);
    }

    @Override
    public synchronized void removeIf(Predicate<IConfigNode> removeCondition) {
        list.removeIf(removeCondition);
    }

    @Override
    public synchronized void removeIf(BiPredicate<String, IConfigNode> removeCondition) {
        map.entrySet().removeIf(entry -> removeCondition.test(entry.getKey(), entry.getValue()));
    }

    @Override
    public synchronized void prepend(IConfigNode child) {
        setTrackingMode(TrackingMode.LIST);
        list.add(0, child);
    }

    @Override
    public synchronized void append(IConfigNode child) {
        setTrackingMode(TrackingMode.LIST);
        list.add(child);
    }

    @Override
    public synchronized Map<String, IConfigNode> asMap() {
        return Collections.unmodifiableMap(map);
    }

    @Override
    public synchronized void put(String childName, IConfigNode child) {
        map.put(childName, child);

        if (child instanceof ConfigNode) {
            ((ConfigNode) child).attachParent(this);
        }

        setTrackingMode(TrackingMode.MAP);
    }

    @Override
    public synchronized IConfigNode remove(String childName) {
        IConfigNode node = map.remove(childName);
        if (node instanceof ConfigNode) {
            ((ConfigNode) node).detachParent();
        }
        return node;
    }

    @Override
    public synchronized boolean collapse() {
        if (isList()) {
            list.removeIf(IConfigNode::collapse);

        } else if (isMap()) {
            map.entrySet().removeIf(entry -> entry.getValue().collapse());
        }

        if (isVirtual()) {
            detachParent();
            return true;
        }
        return false;
    }

    @Override
    protected synchronized void setValue(Object value) {
        super.setValue(value);
        setTrackingMode(value != null ? TrackingMode.PRIMITIVE : TrackingMode.VIRTUAL);
    }

    protected synchronized void reportTrackingChanged(IConfigNode node, TrackingMode mode) {
        if (list.contains(node)) {
            setTrackingMode(TrackingMode.LIST);

        } else if (map.containsValue(node)) {
            setTrackingMode(TrackingMode.MAP);

        } else {
            logger.warn("Non-child node reported to parent node!");
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    protected synchronized ConfigNode detachParent() {
        parent = null;
        return this;
    }

    protected synchronized ConfigNode attachParent(ConfigNode parent) {
        this.parent = parent;
        return this;
    }

    protected synchronized void setTrackingMode(TrackingMode mode) {
        if (this.trackingMode == mode) {
            // tracking mode unchanged. Nothing to do.
            return;
        }

        if (mode == TrackingMode.PRIMITIVE) {
            clearList();
            clearMap();

        } else if (mode == TrackingMode.LIST) {
            clearMap();
            super.setValue(null);

        } else if (mode == TrackingMode.MAP) {
            clearList();
            super.setValue(null);

        } else if (mode == TrackingMode.VIRTUAL) {
            clearList();
            clearMap();
            super.setValue(null);

        } else {
            logger.error("Unaccounted tracking mode! {}", mode);
        }

        this.trackingMode = mode;
        if (parent != null) {
            parent.reportTrackingChanged(this, mode);
        }
    }

    private synchronized void clearList() {
        list.stream()
                .filter(node -> node instanceof ConfigNode)
                .map(ConfigNode.class::cast)
                .forEach(ConfigNode::detachParent);
        list.clear();
    }

    private synchronized void clearMap() {
        map.values()
                .stream()
                .filter(node -> node instanceof ConfigNode)
                .map(ConfigNode.class::cast)
                .forEach(ConfigNode::detachParent);
        map.clear();
    }

    @Override
    public IConfigNode createNewInstance() {
        return new ConfigNode();
    }
}
