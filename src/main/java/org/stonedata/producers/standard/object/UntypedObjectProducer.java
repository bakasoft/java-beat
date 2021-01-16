package org.stonedata.producers.standard.object;

import org.stonedata.errors.StoneException;
import org.stonedata.producers.ObjectProducer;
import org.stonedata.types.object.UntypedObject;

import java.lang.reflect.Type;

public class UntypedObjectProducer implements ObjectProducer {

    public static final UntypedObjectProducer INSTANCE = new UntypedObjectProducer();

    private UntypedObjectProducer() {}

    @Override
    public Object beginInstance() {
        return new UntypedObject();
    }

    @Override
    public void set(Object obj, String key, Object value) {
        if (obj instanceof UntypedObject) {
            ((UntypedObject) obj).set(key, value);
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
