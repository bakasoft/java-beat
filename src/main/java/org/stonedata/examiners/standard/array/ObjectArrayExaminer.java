package org.stonedata.examiners.standard.array;

import org.stonedata.examiners.ArrayExaminer;

import java.lang.reflect.Array;

public class ObjectArrayExaminer implements ArrayExaminer {

    public static final ObjectArrayExaminer INSTANCE = new ObjectArrayExaminer();

    private ObjectArrayExaminer() {}

    @Override
    public int getSizeOf(Object value) {
        return Array.getLength(value);
    }

    @Override
    public Object getValueAt(int index, Object value) {
        return Array.get(value, index);
    }

    @Override
    public String getType() {
        return null;  // Anonymous
    }
}
