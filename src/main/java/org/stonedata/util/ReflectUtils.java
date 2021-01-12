package org.stonedata.util;

import org.stonedata.errors.ConversionException;

public class ReflectUtils {
    public static Object convertTo(Object value, Class<?> type) {
        if (value == null) {
            if (type.isPrimitive()) {
                throw new ConversionException("Cannot convert null to a primitive.");
            }
            return null;
        }
        else if (isCompatible(value, type)) {
            return value;
        }
        else if (type == Character.class && value instanceof String) {
            var str = (String)value;

            if (str.length() != 1) {
                throw new ConversionException("Cannot reduce multi-char string to a single one.");
            }

            return str.charAt(0);
        }
        else if (value instanceof Number && type == Integer.class) {
            return ((Number)value).intValue();
        }
        else {
            throw new ConversionException(value.getClass(), type);
        }
    }

    public static boolean isCompatible(Object value, Class<?> type) {
        return type.isInstance(value)
                || type == boolean.class && value instanceof Boolean;
    }
}
