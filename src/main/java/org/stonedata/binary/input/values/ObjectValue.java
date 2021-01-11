package org.stonedata.binary.input.values;

import org.stonedata.binary.input.Value;
import org.stonedata.binary.input.ValueType;

import java.util.List;
import java.util.Objects;

public class ObjectValue implements Value {

    private final Object value;

    public ObjectValue(Object value) {
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public ValueType getType() {
        return ValueType.OBJECT;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public <T> T asObject(Class<T> type) {
        return type.cast(value);
    }

    @Override
    public List<Value> asList() {
        return List.of(this);
    }
}
