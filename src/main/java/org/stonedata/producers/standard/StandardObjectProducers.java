package org.stonedata.producers.standard;

import org.stonedata.producers.ObjectProducer;
import org.stonedata.producers.standard.object.ClassObjectProducer;
import org.stonedata.producers.standard.object.TypedGenericObjectProducer;
import org.stonedata.producers.standard.object.UntypedGenericObjectProducer;

import java.lang.reflect.Type;
import java.util.Map;

public class StandardObjectProducers {
    private StandardObjectProducers() {}

    public static ObjectProducer tryCreate(Type typeHint) {
        if (typeHint instanceof Class) {
            var typeClass = (Class<?>) typeHint;

            if (Map.class.isAssignableFrom(typeClass)) {
                return UntypedGenericObjectProducer.INSTANCE;
            }
            else if (typeClass.isInterface()) {
                throw new RuntimeException("Cannot instantiate an interface: " + typeClass);
            }

            return new ClassObjectProducer(typeClass);
        }

        return null;
    }
}
