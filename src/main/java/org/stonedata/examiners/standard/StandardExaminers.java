package org.stonedata.examiners.standard;

import org.stonedata.examiners.Examiner;
import org.stonedata.examiners.ValueExaminer;
import org.stonedata.examiners.standard.array.ListExaminer;
import org.stonedata.examiners.standard.array.ObjectArrayExaminer;
import org.stonedata.examiners.standard.object.ClassObjectExaminer;
import org.stonedata.examiners.standard.object.MapExaminer;
import org.stonedata.examiners.standard.value.ClassEnumExaminer;
import org.stonedata.examiners.standard.value.DurationExaminer;
import org.stonedata.types.value.SoftTypedValue;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class StandardExaminers {

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
        else if (type == SoftTypedValue.class) {
            return new ValueExaminer() {
                @Override
                public List<Object> computeArguments(Object value) {
                    return Arrays.asList(((SoftTypedValue)value).getArguments());
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

