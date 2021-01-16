package org.stonedata.producers.standard.value;

import org.stonedata.producers.ValueProducer;
import org.stonedata.types.value.EmptyValue;

import java.util.List;

public class UntypedValueProducer implements ValueProducer {

    public static final UntypedValueProducer INSTANCE = new UntypedValueProducer();

    private UntypedValueProducer() {}

    @Override
    public Object newInstance(List<?> arguments) {
        if (arguments.isEmpty()) {
            return EmptyValue.INSTANCE;
        }
        else if (arguments.size() == 1) {
            return arguments.get(0);
        }
        return arguments;
    }
}
