package org.stonedata.examiners.standard.value;

import org.stonedata.errors.UnsupportedValueException;
import org.stonedata.examiners.ValueExaminer;
import org.stonedata.util.PP;
import org.stonedata.util.Validations;

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
