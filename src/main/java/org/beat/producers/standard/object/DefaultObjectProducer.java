package org.beat.producers.standard.object;

import org.beat.errors.DuplicateKeyException;
import org.beat.producers.ObjectProducer;
import org.beat.types.DefaultObject;
import org.beat.types.standard.DefaultObjectImpl;
import org.beat.util.PP;

import java.lang.reflect.Type;

public class DefaultObjectProducer implements ObjectProducer {

    public static final DefaultObjectProducer INSTANCE = new DefaultObjectProducer();

    private DefaultObjectProducer() {}

    @Override
    public Object beginInstance() {
        return new DefaultObjectImpl();
    }

    @Override
    public void set(Object instance, String key, Object value) {
        var obj = (DefaultObject)instance;

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
