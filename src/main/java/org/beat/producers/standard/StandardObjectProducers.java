package org.beat.producers.standard;

import org.beat.producers.ObjectProducer;
import org.beat.producers.standard.object.ClassObjectProducer;
import org.beat.producers.standard.object.DefaultObjectProducer;
import org.beat.producers.standard.object.DefaultTypedObjectProducer;
import org.beat.producers.standard.object.ClassMapProducer;
import org.beat.producers.standard.object.MapObjectProducer;
import org.beat.util.ReflectUtils;

import java.lang.reflect.Type;
import java.util.Map;

public class StandardObjectProducers {
    private StandardObjectProducers() {}

    public static ObjectProducer create(Type type, String name) {
        return create(type, name, false);
    }

    public static ObjectProducer create(Type typeHint, String typeName, boolean useCleanDefaultTypes) {
        if (typeHint instanceof Class) {
            var typeClass = (Class<?>) typeHint;

            if (Map.class.isAssignableFrom(typeClass)) {
                if (ReflectUtils.canInstantiate(typeClass)) {
                    return new ClassMapProducer(typeClass);
                }
                return MapObjectProducer.INSTANCE;
            }

            if (ReflectUtils.canInstantiate(typeClass)) {
                return new ClassObjectProducer(typeClass);
            }
        }

        if (typeName == null || useCleanDefaultTypes) {
            return DefaultObjectProducer.INSTANCE;
        }
        return new DefaultTypedObjectProducer(typeName);
    }
}
