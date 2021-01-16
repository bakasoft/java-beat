package org.stonedata.types.array;

import java.util.ArrayList;
import java.util.Objects;

public class SoftTypedList extends ArrayList<Object> {

    private final String typeName;

    public SoftTypedList(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SoftTypedList objects = (SoftTypedList) o;
        return Objects.equals(typeName, objects.typeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), typeName);
    }
}
