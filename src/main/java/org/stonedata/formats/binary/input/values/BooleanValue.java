package org.stonedata.formats.binary.input.values;

import org.stonedata.formats.binary.input.Value;
import org.stonedata.formats.binary.input.ValueType;

public class BooleanValue implements Value {
    private final boolean value;

    public BooleanValue(boolean value) {
        this.value = value;
    }

    @Override
    public ValueType getType() {
        return ValueType.BOOLEAN;
    }

    @Override
    public boolean asBoolean() {
        return value;
    }

    @Override
    public String asString() {
        return String.valueOf(value);
    }

    @Override
    public Object getValue() {
        return value;
    }
}
