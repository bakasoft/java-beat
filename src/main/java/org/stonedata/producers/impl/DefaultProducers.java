package org.stonedata.producers.impl;

import org.stonedata.examiners.impl.ClassEnumExaminer;
import org.stonedata.examiners.impl.ClassObjectExaminer;
import org.stonedata.examiners.impl.DurationExaminer;
import org.stonedata.examiners.impl.ListExaminer;
import org.stonedata.examiners.impl.MapExaminer;
import org.stonedata.examiners.impl.ObjectArrayExaminer;
import org.stonedata.producers.ArrayProducer;
import org.stonedata.producers.ObjectProducer;
import org.stonedata.producers.Producer;
import org.stonedata.producers.ValueProducer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class DefaultProducers {
    private DefaultProducers() {}

    public static ObjectProducer tryCreateObjectProducer(Type typeHint) {
        if (typeHint instanceof Class) {
            var typeClass = (Class<?>) typeHint;

            if (Map.class.isAssignableFrom(typeClass)) {
                return GenericObjectProducer.INSTANCE;
            }
            else if (typeClass.isInterface()) {
                throw new RuntimeException("Cannot instantiate an interface: " + typeClass);
            }

            return new ClassObjectProducer(typeClass);
        }

        return null;
    }

    public static ArrayProducer tryCreateArrayProducer(Type typeHint) {
        if (typeHint instanceof ParameterizedType) {
            var pType = (ParameterizedType)typeHint;
            var pArgs = pType.getActualTypeArguments();

            if (pType.getRawType() instanceof Class && pArgs.length == 1) {
                var rawType = (Class<?>) pType.getRawType();
                var pArg0 = pArgs[0];

                if (rawType.isAssignableFrom(List.class) && pArg0 instanceof Class) {
                    var itemType = (Class<?>)pArg0;
                    return new TypedListProducer(itemType);
                }
            }
        }

        return null;
    }

    public static ValueProducer tryCreateValueProducer(Type typeHint) {
        if (typeHint instanceof Class) {
            var typeClass = (Class<?>)typeHint;

            if (typeClass == Integer.class) {
                return new IntegerProducer();
            }
            else if (typeClass == Duration.class) {
                return new DurationProducer();
            }
            else if (typeClass.isEnum()) {
                return new ClassEnumProducer(typeClass);
            }
        }
        return null;
    }

    public static Producer createProducer(Class<?> type) {
        if (type.isArray()) {
            return tryCreateArrayProducer(type);
        }
        else if (type.isEnum()) {
            return tryCreateValueProducer(type);
        }
        else if (List.class.isAssignableFrom(type)) {
            return tryCreateArrayProducer(type);
        }
        else if (Map.class.isAssignableFrom(type)) {
            return tryCreateObjectProducer(type);
        }
        else if (type == Duration.class) {
            return tryCreateValueProducer(type);
        }

        return tryCreateObjectProducer(type);
    }
}
