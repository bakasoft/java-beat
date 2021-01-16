package org.stonedata.types.value;

public class SoftTypedValue {

    private final String typeName;
    private final Object[] arguments;

    public SoftTypedValue(String typeName, Object[] arguments) {
        this.typeName = typeName;
        this.arguments = arguments;
    }

    public String getTypeName() {
        return typeName;
    }

    public Object[] getArguments() {
        return arguments;
    }
}
