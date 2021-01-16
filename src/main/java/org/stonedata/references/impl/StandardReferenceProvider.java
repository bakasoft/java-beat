package org.stonedata.references.impl;

import org.stonedata.references.ReferenceProvider;

import java.util.HashMap;
import java.util.Map;

public class StandardReferenceProvider implements ReferenceProvider {

    private Map<String, Map<Object, String>> data;

    public void setReference(String typeName, Object value, String reference) {
        if (data == null) {
            data = new HashMap<>();
        }
        var map = data.computeIfAbsent(typeName, k -> new HashMap<>());
        if (map.containsKey(value)) {
            throw new RuntimeException();
        }
        map.put(value, reference);
    }

    @Override
    public String getReference(String typeName, Object value) {
        if (data != null) {
            var map = data.get(typeName);
            if (map != null) {
                return map.get(value);
            }
        }
        return null;
    }
}
