package org.stonedata.producers.standard.object;

import org.stonedata.errors.StoneException;
import org.stonedata.producers.ObjectProducer;
import org.stonedata.types.object.SoftTypedObject;

import java.lang.reflect.Type;

public class SoftTypedObjectProducer implements ObjectProducer {

    private final String typeName;

    public SoftTypedObjectProducer(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public Object beginInstance() {
        return new SoftTypedObject(typeName);
    }

    @Override
    public void set(Object obj, String key, Object value) {
        if (obj instanceof SoftTypedObject) {
            ((SoftTypedObject) obj).set(key, value);
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
