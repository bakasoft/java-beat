package org.stonedata.producers.standard.object;

import org.stonedata.errors.DuplicateKeyException;
import org.stonedata.producers.ObjectProducer;
import org.stonedata.types.DefaultTypedObject;
import org.stonedata.types.standard.DefaultTypedObjectImpl;
import org.stonedata.util.PP;

import java.lang.reflect.Type;

public class DefaultTypedObjectProducer  implements ObjectProducer {

    private final String typeName;

    public DefaultTypedObjectProducer(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public Object beginInstance() {
        return new DefaultTypedObjectImpl(typeName);
    }

    @Override
    public void set(Object instance, String key, Object value) {
        var obj = (DefaultTypedObject)instance;

        if (obj.containsKey(key)) {
            throw new DuplicateKeyException("Duplicate key:" + PP.str(key));
        }
        obj.put(key, value);
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
