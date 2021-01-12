package org.stonedata.producers.impl;

import org.stonedata.errors.ProducerNotFoundException;
import org.stonedata.errors.StoneException;
import org.stonedata.producers.ArrayProducer;
import org.stonedata.producers.ObjectProducer;
import org.stonedata.producers.ProducerRepository;
import org.stonedata.producers.ValueProducer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Duration;
import java.util.List;

public class EmptyProducerRepository implements ProducerRepository {
    @Override
    public ObjectProducer findObjectProducer(String type, Type typeHint) {
        if (typeHint instanceof Class) {
            var typeClass = (Class<?>)typeHint;

            return new ClassObjectProducer(typeClass);
        }
        throw new ProducerNotFoundException("object", type, typeHint);
    }

    @Override
    public ArrayProducer findArrayProducer(String type, Type typeHint) {
        if (typeHint instanceof ParameterizedType) {
            var pType = (ParameterizedType)typeHint;
            var pArgs = pType.getActualTypeArguments();

            if (pType.getRawType() instanceof Class && pArgs.length == 1) {
                var rawType = (Class<?>) pType.getRawType();
                var pArg0 = pArgs[0];

                if (rawType.isAssignableFrom(List.class) && pArg0 instanceof Class) {
                    var itemType = (Class<?>)pArg0;
                    return new TypedListProducer(itemType);
                }
            }
        }
        throw new ProducerNotFoundException("array", type, typeHint);
    }

    @Override
    public ValueProducer findValueProducer(String type, Type typeHint) {
        if (typeHint == Integer.class) {
            return new IntegerProducer();
        }
        else if (typeHint == Duration.class) {
            return new DurationProducer();
        }
        throw new ProducerNotFoundException("value", type, typeHint);
    }
}
