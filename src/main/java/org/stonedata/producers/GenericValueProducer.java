package org.stonedata.producers;

import org.stonedata.common.GenericValue;

import java.util.List;

public class GenericValueProducer implements ValueProducer {
    @Override
    public Object newInstance(String type, List<?> arguments) {
        return new GenericValue(type, arguments.toArray());
    }
}
