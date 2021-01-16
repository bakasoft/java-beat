package org.stonedata.examiners.impl;

import org.stonedata.examiners.Examiner;
import org.stonedata.examiners.ValueExaminer;
import org.stonedata.types.GenericValue;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DefaultExaminers {

    public static Examiner createExaminer(Class<?> type, String name) {
        if (type.isArray()) {
            return ObjectArrayExaminer.INSTANCE;  // TODO consider name
        }
        else if (type.isEnum()) {
            return new ClassEnumExaminer(type, name);
        }
        else if (List.class.isAssignableFrom(type)) {
            return ListExaminer.ANONYMOUS_INSTANCE;  // TODO consider name
        }
        else if (Map.class.isAssignableFrom(type)) {
            return MapExaminer.ANONYMOUS_INSTANCE;  // TODO consider name
        }
        else if (type == Duration.class) {
            return new DurationExaminer(name);
        }
        else if (type == GenericValue.class) {
            return new ValueExaminer() {
                @Override
                public List<Object> computeArguments(Object value) {
                    return Arrays.asList(((GenericValue)value).getArguments());
                }

                @Override
                public String getType() {
                    return name;
                }
            };
        }

        return new ClassObjectExaminer(type, name);
    }

}

