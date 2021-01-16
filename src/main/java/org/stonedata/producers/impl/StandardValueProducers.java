package org.stonedata.producers.impl;

import org.stonedata.producers.ValueProducer;

import java.lang.reflect.Type;
import java.time.Duration;


public class StandardValueProducers {
    private StandardValueProducers() {}

    public static ValueProducer create(Type type, String nameHint) {
        if (type instanceof Class) {
            var typeClass = (Class<?>)type;

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

        if (nameHint == null) {
            return UntypedValueProducer.INSTANCE;
        }
        return new TypedValueProducer(nameHint);
    }

}
