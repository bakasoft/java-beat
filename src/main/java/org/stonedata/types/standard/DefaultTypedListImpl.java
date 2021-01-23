package org.stonedata.types.standard;

import org.stonedata.types.DefaultTypedList;

import java.util.ArrayList;
import java.util.Objects;

public class DefaultTypedListImpl extends ArrayList<Object> implements DefaultTypedList {

    private String typeName;

    public DefaultTypedListImpl() {
        this.typeName = null;
    }

    public DefaultTypedListImpl(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DefaultTypedListImpl objects = (DefaultTypedListImpl) o;
        return Objects.equals(typeName, objects.typeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), typeName);
    }

    @Override
    public String toString() {
        if (typeName != null) {
            return typeName + super.toString();
        }
        else {
            return super.toString();
        }
    }
}
