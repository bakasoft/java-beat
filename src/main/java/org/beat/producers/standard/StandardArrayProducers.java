package org.beat.producers.standard;

import org.beat.producers.ArrayProducer;
import org.beat.producers.standard.array.ClassListProducer;
import org.beat.producers.standard.array.DefaultListProducer;
import org.beat.producers.standard.array.DefaultTypedListProducer;
import org.beat.producers.standard.array.HardTypedListProducer;
import org.beat.util.ReflectUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class StandardArrayProducers {

    private StandardArrayProducers() {}

    public static ArrayProducer create(Type type, String name) {
        return create(type, name, false);
    }

    public static ArrayProducer create(Type typeHint, String typeName, boolean useCleanDefaultTypes) {
        if (typeHint instanceof ParameterizedType) {
            var pType = (ParameterizedType)typeHint;
            var pArgs = pType.getActualTypeArguments();

            if (pType.getRawType() instanceof Class && pArgs.length == 1) {
                var rawType = (Class<?>) pType.getRawType();
                var pArg0 = pArgs[0];

                if (rawType.isAssignableFrom(List.class) && pArg0 instanceof Class) {
                    var itemType = (Class<?>)pArg0;
                    return new HardTypedListProducer(itemType);
                }
            }
        }
        else if (typeHint instanceof Class) {
            var typeClass = (Class<?>) typeHint;

            if (List.class.isAssignableFrom(typeClass) && ReflectUtils.canInstantiate(typeClass)) {
                return new ClassListProducer(typeClass);
            }
            // TODO add array classes case
        }

        if (typeName == null || useCleanDefaultTypes) {
            return new DefaultListProducer();
        }
        return new DefaultTypedListProducer(typeName);
    }
}
