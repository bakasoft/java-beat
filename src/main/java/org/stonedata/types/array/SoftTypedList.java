package org.stonedata.types.array;

import java.util.ArrayList;
import java.util.Objects;

public class SoftTypedList extends ArrayList<Object> {

    private final String type;

    public SoftTypedList(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SoftTypedList objects = (SoftTypedList) o;
        return Objects.equals(type, objects.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type);
    }
}
