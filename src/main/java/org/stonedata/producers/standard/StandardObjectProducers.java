package org.stonedata.producers.standard;

import org.stonedata.producers.ObjectProducer;
import org.stonedata.producers.standard.object.HardTypedObjectProducer;
import org.stonedata.producers.standard.object.UntypedObjectProducer;

import java.lang.reflect.Type;
import java.util.Map;

public class StandardObjectProducers {
    private StandardObjectProducers() {}

    public static ObjectProducer tryCreate(Type typeHint) {
        if (typeHint instanceof Class) {
            var typeClass = (Class<?>) typeHint;

            if (Map.class.isAssignableFrom(typeClass)) {
                return UntypedObjectProducer.INSTANCE;
            }
            else if (typeClass.isInterface()) {
                throw new RuntimeException("Cannot instantiate an interface: " + typeClass);
            }

            return new HardTypedObjectProducer(typeClass);
        }

        return null;
    }
}
