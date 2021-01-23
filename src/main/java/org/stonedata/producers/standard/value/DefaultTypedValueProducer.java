package org.stonedata.producers.standard.value;

import org.stonedata.producers.ValueProducer;
import org.stonedata.types.standard.DefaultTypedValueImpl;

import java.util.List;

public class DefaultTypedValueProducer implements ValueProducer {
    private final String typeName;

    public DefaultTypedValueProducer(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public Object newInstance(Object[] arguments) {
        return new DefaultTypedValueImpl(typeName, List.of(arguments));
    }
}
