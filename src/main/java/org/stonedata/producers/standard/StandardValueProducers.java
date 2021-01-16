package org.stonedata.producers.standard;

import org.stonedata.producers.ValueProducer;
import org.stonedata.producers.standard.value.ClassEnumProducer;
import org.stonedata.producers.standard.value.DurationProducer;
import org.stonedata.producers.standard.value.IntegerProducer;

import java.lang.reflect.Type;
import java.time.Duration;


public class StandardValueProducers {
    private StandardValueProducers() {}

    public static ValueProducer tryCreate(Type type) {
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

        return null;
    }

}
