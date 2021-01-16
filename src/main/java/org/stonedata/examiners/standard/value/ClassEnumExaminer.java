package org.stonedata.examiners.standard.value;

import org.stonedata.examiners.ValueExaminer;

import java.util.List;

public class ClassEnumExaminer implements ValueExaminer {
    private final Class<?> type;
    private final String typeName;

    public ClassEnumExaminer(Class<?> type, String typeName) {
        this.type = type;
        this.typeName = typeName;
    }

    @Override
    public String getType() {
        return typeName;
    }

    @Override
    public List<Object> computeArguments(Object value) {
        if (type.isInstance(value)) {
            Enum<?> e = (Enum<?>)value;

            return List.of(e.name());
        }
        else {
            throw new RuntimeException();
        }
    }
}
