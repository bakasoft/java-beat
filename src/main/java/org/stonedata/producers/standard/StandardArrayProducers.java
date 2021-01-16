package org.stonedata.producers.standard;

import org.stonedata.producers.ArrayProducer;
import org.stonedata.producers.standard.array.HardTypedListProducer;
import org.stonedata.producers.standard.array.SoftTypedListProducer;
import org.stonedata.producers.standard.array.UntypedGenericListProducer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class StandardArrayProducers {

    private StandardArrayProducers() {}

    public static ArrayProducer tryCreate(Type type) {
        if (type instanceof ParameterizedType) {
            var pType = (ParameterizedType)type;
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

        // TODO Detect Array Classes, Generic Arrays, Sets, etc.

        return null;
    }
}
