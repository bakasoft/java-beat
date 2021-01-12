package org.stonedata.producers.impl;

import org.stonedata.errors.StoneException;
import org.stonedata.producers.ValueProducer;
import org.stonedata.util.ReflectUtils;

import java.util.List;

public class IntegerProducer implements ValueProducer {
    @Override
    public Object newInstance(String type, List<?> arguments) {
        if (arguments.isEmpty()) {
            return 0;
        }
        else if (arguments.size() == 1) {
            return ReflectUtils.convertTo(arguments.get(0), Integer.class);
        }
        else {
            throw new StoneException("Expected one argument of an integer.");
        }
    }
}
