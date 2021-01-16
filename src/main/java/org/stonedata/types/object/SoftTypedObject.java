package org.stonedata.types.object;

import org.stonedata.errors.StoneException;

import java.util.LinkedHashMap;
import java.util.Objects;

public class SoftTypedObject extends LinkedHashMap<String, Object> {

    private final String typeName;

    public SoftTypedObject(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void set(String key, Object value) {
        if (containsKey(key)) {
            throw new StoneException();
        }
        put(key, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SoftTypedObject that = (SoftTypedObject) o;
        return Objects.equals(typeName, that.typeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), typeName);
    }
}
