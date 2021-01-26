package org.beat.util;

import org.beat.errors.ConversionException;
import org.beat.errors.BeatException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.function.Supplier;

public class ReflectUtils {

    private ReflectUtils() {}

    public static <T> Supplier<T> extractEmptyConstructor(Class<?> typeClass, Class<T> resultClass) {
        if (!resultClass.isAssignableFrom(typeClass)) {
            throw new BeatException();
        }

        for (var constructor : typeClass.getConstructors()) {
            if (constructor.getParameterCount() == 0) {
                if (Modifier.isAbstract(typeClass.getModifiers())) {
                    throw new BeatException();
                }
                return () -> {
                    Object result;
                    try {
                        result = constructor.newInstance();
                    } catch (InstantiationException e) {
                        throw new BeatException(e);
                    }
                    catch (IllegalAccessException e) {
                        throw new BeatException(e);
                    }
                    catch (InvocationTargetException e) {
                        throw new BeatException(e);
                    }
                    return resultClass.cast(result);
                };
            }
        }

        throw new BeatException();
    }
    public static boolean canInstantiate(Class<?> typeClass) {
        // CANNOT if null, interface or abstract
        if (typeClass == null
                || typeClass.isInterface()
                || Modifier.isAbstract(typeClass.getModifiers())) {
            return false;
        }

        // CAN if it has a public constructor without parameters
        for (var constructor : typeClass.getConstructors()) {
            if (constructor.getParameterCount() == 0) {
                return true;
            }
        }
        return false;
    }

    public static String computeDefaultTypeName(Type type) {
        if (type instanceof Class) {
            var typeClass = (Class<?>)type;

            return typeClass.getSimpleName();
        }
        return type.getTypeName();
    }

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
        else if (type == String.class) {
            return value.toString();
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
