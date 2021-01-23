package org.stonedata.examiners;

import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class Examiners {

    public static <T> ValueExaminer value(Class<T> type, Function<T, Object> fn) {
        return value(type, type.getSimpleName(), fn);
    }

    public static <T> ValueExaminer value(Class<T> type, String typeName, Function<T, Object> fn) {
        return new ValueExaminer() {
            @Override
            public Object extractArgument(Object value) {
                if (type.isInstance(value)) {
                    return fn.apply(type.cast(value));
                }
                else {
                    throw new RuntimeException();
                }
            }

            @Override
            public String getType() {
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
            public String getType() {
                return typeName;
            }
        };
    }
}
