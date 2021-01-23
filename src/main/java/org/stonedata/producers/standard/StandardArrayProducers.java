package org.stonedata.producers.standard;

import org.stonedata.producers.ArrayProducer;
import org.stonedata.producers.standard.array.ClassListProducer;
import org.stonedata.producers.standard.array.DefaultListProducer;
import org.stonedata.producers.standard.array.DefaultTypedListProducer;
import org.stonedata.producers.standard.array.HardTypedListProducer;
import org.stonedata.util.ReflectUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class StandardArrayProducers {

    private StandardArrayProducers() {}

    public static ArrayProducer create(Type typeHint) {
        return create(null, typeHint, false);
    }

    public static ArrayProducer create(String typeName, Type typeHint, boolean useCleanDefaultTypes) {
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
