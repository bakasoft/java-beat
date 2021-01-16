package org.stonedata.references.impl;

import org.stonedata.errors.UnknownReferenceException;
import org.stonedata.references.ReferenceTracker;

import java.util.HashMap;
import java.util.Map;

public class StandardReferenceTracker implements ReferenceTracker {

    private Map<String, Map<String, Object>> data;

    public StandardReferenceTracker() {
        data = null;
    }

    @Override
    public Object retrieve(String typeName, String reference) {
        if (data != null) {
            var map = data.get(typeName);
            if (map != null && map.containsKey(reference)) {
                return map.get(reference);
            }
        }
        throw new UnknownReferenceException(typeName, reference);
    }

    @Override
    public void store(String typeName, Object value, String reference) {
        if (data == null) {
            data = new HashMap<>();
        }
        var map = data.computeIfAbsent(typeName, k -> new HashMap<>());
        if (map.containsKey(reference)) {
            throw new RuntimeException();
        }
        map.put(reference, value);
    }
}
