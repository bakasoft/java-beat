package org.stonedata.producers.standard;

import org.stonedata.producers.ValueProducer;
import org.stonedata.producers.standard.value.ClassEnumProducer;
import org.stonedata.producers.standard.value.DurationProducer;
import org.stonedata.producers.standard.value.IntegerProducer;
import org.stonedata.producers.standard.value.TypedValueProducer;
import org.stonedata.producers.standard.value.UntypedValueProducer;

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
