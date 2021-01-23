package org.stonedata.producers.standard;

import org.stonedata.producers.ObjectProducer;
import org.stonedata.producers.standard.object.ClassObjectProducer;
import org.stonedata.producers.standard.object.DefaultObjectProducer;
import org.stonedata.producers.standard.object.DefaultTypedObjectProducer;
import org.stonedata.producers.standard.object.ClassMapProducer;
import org.stonedata.producers.standard.object.MapObjectProducer;
import org.stonedata.util.ReflectUtils;

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
