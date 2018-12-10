package de.mlessmann.confort.migration;

import java.util.HashMap;
import java.util.Map;

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
}
