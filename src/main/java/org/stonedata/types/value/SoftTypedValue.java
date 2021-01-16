package org.stonedata.types.value;

public class SoftTypedValue {

    private final String type;
    private final Object[] arguments;

    public SoftTypedValue(String type, Object[] arguments) {
        this.type = type;
        this.arguments = arguments;
    }

    public String getType() {
        return type;
    }

    public Object[] getArguments() {
        return arguments;
    }
}
