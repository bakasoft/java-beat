package org.stonedata.producers.impl;

import org.stonedata.producers.ObjectProducer;

import java.lang.reflect.Type;
import java.util.Map;

public class StandardObjectProducers {
    private StandardObjectProducers() {}

    public static ObjectProducer create(Type typeHint, String typeName) {
        if (typeHint instanceof Class) {
            var typeClass = (Class<?>) typeHint;

            if (Map.class.isAssignableFrom(typeClass)) {
                if (typeName == null) {
                    return UntypedGenericObjectProducer.INSTANCE;
                }
                return new TypedGenericObjectProducer(typeName);
            }
            else if (typeClass.isInterface()) {
                throw new RuntimeException("Cannot instantiate an interface: " + typeClass);
            }

            return new ClassObjectProducer(typeClass);
        }

        if (typeName == null) {
            return UntypedGenericObjectProducer.INSTANCE;
        }
        return new TypedGenericObjectProducer(typeName);
    }
}
