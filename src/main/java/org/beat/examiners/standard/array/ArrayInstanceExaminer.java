package org.beat.examiners.standard.array;

import org.beat.examiners.ArrayExaminer;

import java.lang.reflect.Array;

public class ArrayInstanceExaminer implements ArrayExaminer {

    public static final ArrayInstanceExaminer ANONYMOUS_INSTANCE = new ArrayInstanceExaminer(null);

    private final String typeName;

    public ArrayInstanceExaminer(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public int getSizeOf(Object value) {
        return Array.getLength(value);
    }

    @Override
    public Object getValueAt(int index, Object value) {
        return Array.get(value, index);
    }

    @Override
    public String getTypeName() {
        return typeName;
    }
}
