package org.beat.types.standard;

import org.beat.types.DefaultTypedValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DefaultTypedValueImpl extends ArrayList<Object> implements DefaultTypedValue {

    private String typeName;

    public DefaultTypedValueImpl() {
        this.typeName = null;
    }

    public DefaultTypedValueImpl(String typeName) {
        this.typeName = typeName;
    }

    public DefaultTypedValueImpl(List<Object> arguments) {
        super(arguments);
        this.typeName = null;
    }

    public DefaultTypedValueImpl(String typeName, List<Object> arguments) {
        super(arguments);
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
        DefaultTypedValueImpl objects = (DefaultTypedValueImpl) o;
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
