package org.stonedata.producers.standard.value;

import org.stonedata.errors.UnsupportedValueException;
import org.stonedata.producers.ValueProducer;
import org.stonedata.util.PP;
import org.stonedata.util.Validations;

import java.util.Objects;

public class ClassEnumProducer implements ValueProducer {
    private final Class<?> enumType;
    private final Enum<?>[] enumConstants;

    public ClassEnumProducer(Class<?> enumType) {
        Validations.requireAssignableFrom(Enum.class, enumType);
        this.enumType = enumType;
        this.enumConstants = extractConstants(enumType);
    }

    private Object findByName(String name) {
        for (var item : enumConstants) {
            if (Objects.equals(name, item.name())) {
                return item;
            }
        }

        throw new UnsupportedValueException(String.format(
                "Value %s was not found in Enum %s.",
                PP.str(name), PP.type(enumType)
        ));
    }

    @Override
    public Object newInstance(Object[] arguments) {
        if (arguments.length != 1) {
            throw new UnsupportedValueException(String.format(
                    "Expected only one argument instead of %s.",
                    PP.str(arguments)
            ));
        }

        var arg = arguments[0];

        if (arg instanceof String) {
            return findByName((String)arg);
        }
        else {
            throw new UnsupportedValueException(String.format(
                    "Expected a string value instead of %s.",
                    PP.str(arg)
            ));
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
