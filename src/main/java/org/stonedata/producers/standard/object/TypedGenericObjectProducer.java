package org.stonedata.producers.standard.object;

import org.stonedata.errors.StoneException;
import org.stonedata.producers.ObjectProducer;
import org.stonedata.types.GenericObject;

import java.lang.reflect.Type;

public class TypedGenericObjectProducer implements ObjectProducer {

    private final String typeName;

    public TypedGenericObjectProducer(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public Object beginInstance() {
        return new GenericObject(typeName);
    }

    @Override
    public void set(Object obj, String key, Object value) {
        if (obj instanceof GenericObject) {
            ((GenericObject) obj).set(key, value);
        }
        else {
            throw new StoneException();
        }
    }

    @Override
    public Type getTypeHint(String key) {
        return null;
    }

    @Override
    public Object endInstance(Object instance) {
        return instance;
    }
}
