package org.stonedata.producers.impl;

import org.stonedata.types.EmptyValue;
import org.stonedata.types.GenericValue;
import org.stonedata.producers.ValueProducer;

import java.util.List;

public class GenericValueProducer implements ValueProducer {

    public static final GenericValueProducer INSTANCE = new GenericValueProducer();

    private GenericValueProducer() {}

    @Override
    public Object newInstance(String type, List<?> arguments) {
        if (type == null) {
            if (arguments.isEmpty()) {
                return EmptyValue.INSTANCE;
            }
            else if (arguments.size() == 1) {
                return arguments.get(0);
            }
            return arguments;
        }
        return new GenericValue(type, arguments.toArray());
    }
}
