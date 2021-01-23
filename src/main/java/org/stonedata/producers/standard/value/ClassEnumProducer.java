package org.stonedata.producers.standard.value;

import org.stonedata.producers.ValueProducer;

import java.util.List;
import java.util.Objects;

public class ClassEnumProducer implements ValueProducer {
    private final Enum<?>[] enumConstants;

    public ClassEnumProducer(Class<?> enumType) {
        this.enumConstants = extractConstants(enumType);
    }

    private Object findByName(String name) {
        for (var item : enumConstants) {
            if (Objects.equals(name, item.name())) {
                return item;
            }
        }

        throw new RuntimeException("enum value not found: " + name);
    }

    @Override
    public Object newInstance(Object[] arguments) {
        if (arguments.length != 1) {
            throw new RuntimeException();
        }

        var arg = arguments[0];

        if (arg instanceof String) {
            return findByName((String)arg);
        }
        else {
            throw new RuntimeException();
        }
    }

    private static Enum<?>[] extractConstants(Class<?> enumType) {
        var values = enumType.getEnumConstants();
        var result = new Enum<?>[values.length];

        for (int i = 0; i < values.length; i++) {
            result[i] = (Enum<?>)values[i];
        }

        return result;
    }
}
