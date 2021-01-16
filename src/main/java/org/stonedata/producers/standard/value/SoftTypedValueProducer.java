package org.stonedata.producers.standard.value;

import org.stonedata.types.value.SoftTypedValue;
import org.stonedata.producers.ValueProducer;

import java.util.List;

public class SoftTypedValueProducer implements ValueProducer {

    private final String typeName;

    public SoftTypedValueProducer(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public Object newInstance(List<?> arguments) {
        return new SoftTypedValue(typeName, arguments.toArray());
    }
}
