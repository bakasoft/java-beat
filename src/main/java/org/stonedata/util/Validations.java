package org.stonedata.util;

import org.stonedata.errors.UnsupportedValueException;

public class Validations {

    private Validations() {}

    public static void requireAssignableFrom(Class<?> toClass, Class<?> fromClass) {
        if (!toClass.isAssignableFrom(fromClass)) {
            throw new UnsupportedValueException(String.format(
                    "Class %s is not supported, expected to be assignable from: %s",
                    PP.type(fromClass),
                    PP.type(toClass)
            ));
        }
    }

}
