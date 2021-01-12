package org.stonedata.producers.impl;

import org.stonedata.producers.ArrayProducer;
import org.stonedata.producers.ObjectProducer;
import org.stonedata.producers.ValueProducer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Duration;
import java.util.List;

public class DefaultProducers {
    private DefaultProducers() {}

    public static ObjectProducer tryCreateObjectProducer(Type typeHint) {
        if (typeHint instanceof Class) {
            var typeClass = (Class<?>) typeHint;

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
        if (typeHint == Integer.class) {
            return new IntegerProducer();
        }
        else if (typeHint == Duration.class) {
            return new DurationProducer();
        }
        return null;
    }
}
