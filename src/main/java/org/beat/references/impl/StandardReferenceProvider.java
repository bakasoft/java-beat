package org.beat.references.impl;

import org.beat.references.ReferenceProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class StandardReferenceProvider implements ReferenceProvider {

    private Map<Object, String> valueReferences;
    private List<Function<Object,String>> generators;

    public void setReference(Object value, String reference) {
        if (valueReferences == null) {
            valueReferences = new HashMap<>();
        }
        valueReferences.put(value, reference);
    }

    public void addGenerator(Function<Object, String> generator) {
        if (generators == null) {
            generators = new ArrayList<>();
        }
        generators.add(generator);
    }

    @Override
    public String getReference(Object value) {
        if (valueReferences != null) {
            var ref = valueReferences.get(value);
            if (ref != null) {
                return ref;
            }
        }
        if (generators != null) {
            for (var fn : generators) {
                var ref = fn.apply(value);
                if (ref != null) {
                    return ref;
                }
            }
        }
        return null;
    }
}
