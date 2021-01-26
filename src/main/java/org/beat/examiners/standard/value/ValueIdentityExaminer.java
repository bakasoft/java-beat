package org.beat.examiners.standard.value;

import org.beat.examiners.ValueExaminer;

public class ValueIdentityExaminer implements ValueExaminer {

    public static final ValueIdentityExaminer INSTANCE = new ValueIdentityExaminer();

    private ValueIdentityExaminer() {}

    @Override
    public String getTypeName() {
        return null;
    }

    @Override
    public Object extractArgument(Object value) {
        return value;
    }
}
