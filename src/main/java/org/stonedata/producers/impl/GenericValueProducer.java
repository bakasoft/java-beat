package org.stonedata.producers.impl;

import org.stonedata.util.GenericValue;
import org.stonedata.producers.ValueProducer;

import java.util.List;

public class GenericValueProducer implements ValueProducer {

    public static final GenericValueProducer INSTANCE = new GenericValueProducer();

    private GenericValueProducer() {}

    @Override
    public Object newInstance(String type, List<?> arguments) {
        return new GenericValue(type, arguments.toArray());
    }
}
