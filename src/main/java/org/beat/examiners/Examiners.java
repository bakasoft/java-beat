package org.beat.examiners;

import org.beat.errors.UnsupportedValueException;
import org.beat.util.PP;
import org.beat.util.ReflectUtils;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public class Examiners {

    private Examiners() {}

    public static <T> ValueExaminer value(Class<T> type, Function<T, Object> fn) {
        return value(type, ReflectUtils.computeDefaultTypeName(type), fn);
    }

    public static <T> ValueExaminer value(Class<T> type, String typeName, Function<T, Object> fn) {
        return new ValueExaminer() {
            @Override
            public Object extractArgument(Object value) {
                if (type.isInstance(value)) {
                    return fn.apply(type.cast(value));
                }
                else {
                    throw new UnsupportedValueException(PP.type(type), PP.str(value));
                }
            }

            @Override
            public String getTypeName() {
                return typeName;
            }
        };
    }

    public static ValueExaminer value(UnaryOperator<Object> fn) {
        return value((String)null, fn);
    }

    public static ValueExaminer value(String typeName, UnaryOperator<Object> fn) {
        return new ValueExaminer() {
            @Override
            public Object extractArgument(Object value) {
                return fn.apply(value);
            }

            @Override
            public String getTypeName() {
                return typeName;
            }
        };
    }
}
