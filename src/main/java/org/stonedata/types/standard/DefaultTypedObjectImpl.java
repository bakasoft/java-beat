package org.stonedata.types.standard;

import org.stonedata.types.DefaultTypedObject;

import java.util.LinkedHashMap;
import java.util.Objects;

public class DefaultTypedObjectImpl extends LinkedHashMap<String, Object> implements DefaultTypedObject {

    private String typeName;

    public DefaultTypedObjectImpl() {
        this.typeName = null;
    }

    public DefaultTypedObjectImpl(String typeName) {
        this.typeName = typeName;
    }

    public void setTypeName(String typeName) {
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
        DefaultTypedObjectImpl that = (DefaultTypedObjectImpl) o;
        return Objects.equals(typeName, that.typeName);
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
