package org.stonedata.examiners.standard.value;

import org.stonedata.examiners.ValueExaminer;

public class DefaultTypedValueExaminer implements ValueExaminer {

    private final String typeName;

    public DefaultTypedValueExaminer(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String getTypeName() {
        return typeName;
    }

    @Override
    public Object extractArgument(Object value) {
        return value;
    }
}
