package de.mlessmann.confort.node;

import de.mlessmann.confort.ValueHolder;
import de.mlessmann.confort.api.IConfigNode;
import de.mlessmann.confort.api.except.TypeMismatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class ConfigNode extends ValueHolder implements IConfigNode {

    private static final Logger logger = LoggerFactory.getLogger(ConfigNode.class);

    private ConfigNode parent = null;
    private Map<String, IConfigNode> map = new LinkedHashMap<>();
    private List<IConfigNode> list = new LinkedList<>();
    private TrackingMode trackingMode = TrackingMode.VIRTUAL;

    @Override
    public synchronized boolean isMap() {
        return trackingMode == TrackingMode.MAP || trackingMode == TrackingMode.EXPLICIT_MAP;
    }

    @Override
    public synchronized Optional<Map<String, IConfigNode>> optMap() {
        return Optional.ofNullable(isMap() ? asMap() : null);
    }

    @Override
    public synchronized boolean isList() {
        return trackingMode == TrackingMode.LIST || trackingMode == TrackingMode.EXPLICIT_LIST;
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
        if (trackingMode == TrackingMode.VIRTUAL || trackingMode == TrackingMode.EXPLICIT_NULL) {
            return true;

        } else if (trackingMode == TrackingMode.EXPLICIT_LIST || trackingMode == TrackingMode.EXPLICIT_MAP) {
            return false;

        } else if (trackingMode == TrackingMode.LIST) {
            return list.isEmpty() || list.stream().allMatch(IConfigNode::isVirtual);

        } else if (trackingMode == TrackingMode.MAP) {
            return map.isEmpty() || map.values().stream().allMatch(IConfigNode::isVirtual);

        } else if (trackingMode == TrackingMode.PRIMITIVE) {
            return getValue() == null;
        } else {
            logger.error("Unaccounted tracking mode! {} at {}", trackingMode, getFullPath());
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
    public <T> List<T> asValueList(Class<T> type) {
        List<T> results = new LinkedList<>();
        optList().orElseThrow(() -> supplyNotA("list"))
                .stream()
                .map(node -> node.getValue(type))
                .forEach(value -> {
                    if (value == null) {
                        throwIncompatibleType(type);
                    }
                    results.add(value);
                });

        return Collections.unmodifiableList(results);
    }

    @Override
    public <T> List<T> optValueList(Class<T> type, boolean ignoreCompatibility) {
        List<T> results = new LinkedList<>();
        optList().ifPresent(
                list ->
                        list.stream()
                                .map(node -> node.getValue(type))
                                .filter(node -> ignoreCompatibility || node != null)
                                .forEach(results::add)
        );
        return Collections.unmodifiableList(results);
    }

    @Override
    public <T> List<T> optValueList(Class<T> type) {
        return optValueList(type, false);
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
    public void prependValue(Object value) {
        if (value instanceof IConfigNode) {
            value = ((IConfigNode) value).getValue();
        }

        final ConfigNode child = new ConfigNode();
        child.setValue(value);
        prepend(child);
    }

    @Override
    public synchronized void append(IConfigNode child) {
        setTrackingMode(TrackingMode.LIST);
        list.add(child);
    }

    @Override
    public void appendValue(Object value) {
        if (value instanceof IConfigNode) {
            value = ((IConfigNode) value).getValue();
        }

        final ConfigNode child = new ConfigNode();
        child.setValue(value);
        append(child);
    }

    @Override
    public synchronized Map<String, IConfigNode> asMap() {
        return Collections.unmodifiableMap(map);
    }

    @Override
    public <T> Map<String, T> asValueMap(Class<T> type) {
        Map<String, T> resultMap = new LinkedHashMap<>();
        optMap().orElseThrow(() -> supplyNotA("map"))
                .forEach((k, v) -> {
                    final T castValue = v.getValue(type);
                    if (castValue == null) {
                        throwIncompatibleType(type);
                    }
                    resultMap.put(k, castValue);
                });
        return Collections.unmodifiableMap(resultMap);
    }

    @Override
    public <T> Map<String, T> optValueMap(Class<T> type) {
        Map<String, T> resultMap = new LinkedHashMap<>();
        optMap().ifPresent(map -> {
            map.forEach((k, v) -> {
                final T castValue = v.getValue(type);
                if (castValue != null) {
                    resultMap.put(k, castValue);
                }
            });
        });
        return Collections.unmodifiableMap(resultMap);
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
    public void putValue(String childName, Object value) {
        if (value instanceof IConfigNode) {
            value = ((IConfigNode) value).getValue();
        }

        final ConfigNode child = new ConfigNode();
        child.setValue(value);
        put(childName, child);
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
    public synchronized IConfigNode remove(String... path) {
        if (path == null) {
            throw new IllegalArgumentException("Child path cannot be null for #remove");
        }
        if (path.length == 1) {
            return this.remove(path[0]);
        } else if (path.length > 1) {
            String[] subPath = Arrays.copyOf(path, path.length - 1);
            return this.getNode(subPath).remove(path[path.length - 1]);
        } else {
            throw new IllegalArgumentException("Invalid path length: " + path.length);
        }
    }

    @Override
    public synchronized boolean collapse() {
        if (isList()) {
            removeIf(IConfigNode::collapse);

        } else if (isMap()) {
            removeIf((k, v) -> v.collapse());
        }

        // Do not detach when we are told to track NULL!
        if (isVirtual() && trackingMode != TrackingMode.EXPLICIT_NULL) {
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

    @Override
    public synchronized void setNull() {
        setTrackingMode(TrackingMode.EXPLICIT_NULL);
    }

    @Override
    public void setList() {
        setTrackingMode(TrackingMode.EXPLICIT_LIST);
    }

    @Override
    public void setMap() {
        setTrackingMode(TrackingMode.EXPLICIT_MAP);
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

        } else if (mode == TrackingMode.LIST || mode == TrackingMode.EXPLICIT_LIST) {
            clearMap();
            super.setValue(null);

        } else if (mode == TrackingMode.MAP || mode == TrackingMode.EXPLICIT_MAP) {
            clearList();
            super.setValue(null);

        } else if (mode == TrackingMode.VIRTUAL || mode == TrackingMode.EXPLICIT_NULL) {
            clearList();
            clearMap();
            super.setValue(null);

        } else {
            logger.error("Unaccounted tracking mode ({}) at {}", mode, getFullPath());
        }

        this.trackingMode = mode;
        if (parent != null) {
            parent.reportTrackingChanged(this, mode);
        }
    }

    private synchronized void clearList() {
        list.stream()
                .filter(ConfigNode.class::isInstance)
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
    public synchronized void clear() {
        clearMap();
        clearList();

        if (trackingMode == TrackingMode.LIST) {
            setTrackingMode(TrackingMode.EXPLICIT_LIST);
        } else if (trackingMode == TrackingMode.MAP) {
            setTrackingMode(TrackingMode.EXPLICIT_MAP);
        }
    }

    @Override
    public IConfigNode createNewInstance() {
        return new ConfigNode();
    }

    private TypeMismatchException supplyNotA(String type) {
        final String msg = String.format("Node is not a %s at: %s", type, getFullPath());
        final TypeMismatchException exception = new TypeMismatchException(msg);
        logger.debug(msg, exception);
        return exception;
    }

    private <T> void throwIncompatibleType(Class<T> type) {
        final String msg = String.format("Incompatible value encountered. Wanted: %s at: %s", type.getName(), getFullPath());
        final TypeMismatchException exception = new TypeMismatchException(msg);
        logger.debug(msg, exception);
        throw exception;
    }

    private synchronized String getChildKey(ConfigNode child) {
        if (trackingMode == TrackingMode.MAP || trackingMode == TrackingMode.EXPLICIT_MAP) {
            return map.entrySet().stream()
                    .filter(entry -> entry.getValue() == child)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Given node is not my child!"))
                    .getKey();
        } else if (trackingMode == TrackingMode.LIST || trackingMode == TrackingMode.EXPLICIT_LIST) {
            AtomicInteger idx = new AtomicInteger();
            final Optional<IConfigNode> optChild = list.stream()
                    .map(theChild -> {
                        idx.incrementAndGet();
                        return theChild == child ? theChild : null;
                    })
                    .filter(Objects::nonNull)
                    .findFirst();
            if (!optChild.isPresent()) {
                throw new IllegalArgumentException("Given node is not my child!");
            }
            return String.format("[%d]", idx.get());
        }
        throw new IllegalStateException("I don't have any children!");
    }

    private String getFullPath() {
        return parent != null ? parent.getFullPath() + "." + parent.getChildKey(this) : "$";
    }
}
