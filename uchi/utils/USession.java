package uchi.utils;

import java.util.HashMap;
import java.util.Map;

public class USession {
    private Map<String, Object> values;

    public USession() {
        this.values = new HashMap<>();
    }

    public USession(Map<String, Object> values) {
        this.values = new HashMap<>(values);
    }

    public void set(String key, Object value) {
        values.put(key, value);
    }

    public Object get(String key) {
        return values.get(key);
    }

    public void remove(String key) {
        values.remove(key);
    }

    public boolean contains(String key) {
        return values.containsKey(key);
    }

    public Map<String, Object> getValues() {
        return new HashMap<>(values);
    }
}
