package org.stonedata.producers.standard.value;

import org.stonedata.errors.StoneException;
import org.stonedata.producers.ValueProducer;
import org.stonedata.util.ReflectUtils;

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
            throw new StoneException("Expected one argument of an integer.");
        }
    }
}
