package org.beat.examiners.standard.value;

import org.beat.errors.UnsupportedValueException;
import org.beat.examiners.ValueExaminer;
import org.beat.util.PP;
import org.beat.util.Validations;

public class ClassEnumExaminer implements ValueExaminer {
    private final Class<?> enumType;
    private final String typeName;

    public ClassEnumExaminer(Class<?> enumType, String typeName) {
        Validations.requireAssignableFrom(Enum.class, enumType);
        this.enumType = enumType;
        this.typeName = typeName;
    }

    @Override
    public String getTypeName() {
        return typeName;
    }

    @Override
    public Object extractArgument(Object value) {
        if (enumType.isInstance(value)) {
            var e = (Enum<?>)value;

            return e.name();
        }
        else {
            throw new UnsupportedValueException(
                    PP.type(enumType), PP.str(value));
        }
    }
}
