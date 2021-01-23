package org.stonedata.examiners.standard;

import org.stonedata.errors.StoneException;
import org.stonedata.examiners.Examiner;
import org.stonedata.examiners.standard.array.ArrayInstanceExaminer;
import org.stonedata.examiners.standard.array.ListExaminer;
import org.stonedata.examiners.standard.object.ClassObjectExaminer;
import org.stonedata.examiners.standard.object.MapExaminer;
import org.stonedata.examiners.standard.value.ClassEnumExaminer;
import org.stonedata.examiners.standard.value.DefaultTypedValueExaminer;
import org.stonedata.examiners.standard.value.ValueIdentityExaminer;
import org.stonedata.types.DefaultTypedList;
import org.stonedata.types.DefaultTypedObject;
import org.stonedata.types.DefaultTypedValue;
import org.stonedata.types.DefaultValue;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StandardExaminers {

    private StandardExaminers() {}

    public static Examiner create(Object value) {
        if (value == null
                || value instanceof String
                || value instanceof Boolean
                || value instanceof Number
                || value instanceof Character) {
            return ValueIdentityExaminer.INSTANCE;
        }
        else if (value instanceof DefaultTypedObject) {
            var typeName = ((DefaultTypedObject)value).getTypeName();
            if (typeName == null) {
                return MapExaminer.ANONYMOUS_INSTANCE;
            }
            return new MapExaminer(typeName);
        }
        else if (value instanceof DefaultTypedList) {
            var typeName = ((DefaultTypedList)value).getTypeName();
            if (typeName == null) {
                return ListExaminer.ANONYMOUS_INSTANCE;
            }
            return new ListExaminer(typeName);
        }
        else if (value instanceof DefaultTypedValue) {
            var typeName = ((DefaultTypedValue)value).getTypeName();
            if (typeName == null) {
                return ValueIdentityExaminer.INSTANCE;
            }
            return new DefaultTypedValueExaminer(typeName);
        }
        return createFromType(value.getClass(), null);
    }

    private static Examiner createFromType(Class<?> typeClass, String typeName) {
        return tryCreateValue(typeClass, typeName)
                .or(() -> tryCreateArray(typeClass, typeName))
                .orElseGet(() -> createObject(typeClass, typeName));
    }

    public static Examiner createObject(Class<?> typeClass, String typeName) {
        // TODO consider names
        if (Map.class.isAssignableFrom(typeClass)) {
            return MapExaminer.ANONYMOUS_INSTANCE;
        }
        return new ClassObjectExaminer(typeClass, typeName);
    }

    public static Examiner createArray(Class<?> typeClass, String typeName) {
        return tryCreateArray(typeClass, typeName).orElseThrow(StoneException::new);
    }

    public static Examiner createValue(Class<?> typeClass, String typeName) {
        return tryCreateValue(typeClass, typeName).orElseThrow(StoneException::new);
    }

    public static Optional<Examiner> tryCreateArray(Class<?> typeClass, String typeName) {
        // TODO consider names
        if (List.class.isAssignableFrom(typeClass)) {
            return Optional.of(ListExaminer.ANONYMOUS_INSTANCE);
        }
        else if (typeClass.isArray()) {
            return Optional.of(ArrayInstanceExaminer.ANONYMOUS_INSTANCE);
        }
        return Optional.empty();
    }

    public static Optional<Examiner> tryCreateValue(Class<?> typeClass, String typeName) {
        // TODO consider names
        if (DefaultValue.class.isAssignableFrom(typeClass)) {
            return Optional.of(ValueIdentityExaminer.INSTANCE);
        }
        else if (typeClass.isEnum()) {
            return Optional.of(new ClassEnumExaminer(typeClass, typeName));
        }
        return Optional.empty();
    }
}

