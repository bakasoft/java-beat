package org.beat.producers.standard.value;

import org.beat.errors.BeatException;
import org.beat.producers.ValueProducer;
import org.beat.util.ReflectUtils;

public class IntegerProducer implements ValueProducer {
    @Override
    public Object newInstance(Object[] arguments) {
        if (arguments.length == 0) {
            return 0;
        }
        else if (arguments.length == 1) {
            return ReflectUtils.convertTo(arguments[0], Integer.class);
        }
        else {
            throw new BeatException("Expected one argument of an integer.");
        }
    }
}
