package org.stonedata.producers.standard.value;

import org.stonedata.types.GenericValue;
import org.stonedata.producers.ValueProducer;

import java.util.List;

public class TypedValueProducer implements ValueProducer {

    private final String typeName;

    public TypedValueProducer(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public Object newInstance(List<?> arguments) {
        return new GenericValue(typeName, arguments.toArray());
    }
}
