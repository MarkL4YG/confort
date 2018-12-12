package de.mlessmann.confort.migration;

import de.mlessmann.confort.migration.nodes.interaction.NodeGenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DescriptorScope {

    private Map<String, String> scope = new HashMap<>();

    public boolean has(String key) {
        return scope.containsKey(key);
    }

    public void set(String key, String value) {
        scope.put(key, value);
    }

    public String get(String key) {
        return scope.getOrDefault(key, null);
    }

    public Optional<NodeGenerator> optGenerator(String identifier) {
        return Optional.empty();
    }
}
