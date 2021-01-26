package org.beat.references.impl;

import org.beat.errors.UnknownReferenceException;
import org.beat.references.ReferenceTracker;

import java.util.HashMap;
import java.util.Map;

public class StandardReferenceTracker implements ReferenceTracker {

    private Map<String, Object> referenceValues;

    @Override
    public Object retrieve(String reference) {
        if (referenceValues != null && referenceValues.containsKey(reference)) {
            return referenceValues.get(reference);
        }
        else {
            throw new UnknownReferenceException(reference);
        }
    }

    @Override
    public void store(String reference, Object value) {
        if (referenceValues == null) {
            referenceValues = new HashMap<>();
        }
        referenceValues.put(reference, value);
    }
}
