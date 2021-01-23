package org.stonedata.examiners.standard.array;

import org.stonedata.examiners.ArrayExaminer;

import java.util.List;

public class ListExaminer implements ArrayExaminer {

    public static final ListExaminer ANONYMOUS_INSTANCE = new ListExaminer(null);

    private final String type;

    public ListExaminer(String type) {
        this.type = type;
    }

    private static List<?> list(Object any) {
        return (List<?>)any;
    }

    @Override
    public int getSizeOf(Object value) {
        return list(value).size();
    }

    @Override
    public Object getValueAt(int index, Object value) {
        return list(value).get(index);
    }

    @Override
    public String getTypeName() {
        return type;
    }
}
