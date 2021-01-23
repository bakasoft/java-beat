package org.stonedata.producers.standard.value;

import org.stonedata.producers.ValueProducer;
import org.stonedata.types.standard.DefaultValueImpl;

import java.util.List;

public class DefaultValueProducer implements ValueProducer {
    public static final DefaultValueProducer INSTANCE = new DefaultValueProducer();

    private DefaultValueProducer() {}

    @Override
    public Object newInstance(Object[] arguments) {
        if (arguments.length == 1) {
            return arguments[0];
        }
        return new DefaultValueImpl(List.of(arguments));
    }
}
