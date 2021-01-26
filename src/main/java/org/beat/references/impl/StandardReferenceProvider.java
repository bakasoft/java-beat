package org.beat.references.impl;

import org.beat.references.ReferenceProvider;

import java.util.HashMap;
import java.util.Map;

public class StandardReferenceProvider implements ReferenceProvider {

    private Map<Object, String> valueReferences;

    public void setReference(Object value, String reference) {
        if (valueReferences == null) {
            valueReferences = new HashMap<>();
        }
        valueReferences.put(value, reference);
    }

    @Override
    public String getReference(Object value) {
        if (valueReferences == null) {
            return null;
        }
        return valueReferences.get(value);
    }
}
