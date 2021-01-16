package org.stonedata.producers.standard.array;

import org.stonedata.Stone;
import org.stonedata.errors.StoneException;
import org.stonedata.producers.ArrayProducer;
import org.stonedata.types.array.HardTypedList;

import java.lang.reflect.Type;
import java.util.List;

public class ClassListProducer implements ArrayProducer {
    private final Class<?> typeClass;

    public ClassListProducer(Class<?> typeClass) {
        this.typeClass = typeClass;
    }

    @Override
    public Object beginInstance() {
        try {
            return typeClass.newInstance();
        } catch (InstantiationException e) {
            throw new StoneException(e);
        } catch (IllegalAccessException e) {
            throw new StoneException(e);
        }
    }

    @Override
    public void add(Object instance, Object item) {
        HardTypedListProducer.forcedAdd((List<?>)instance, item);
    }

    @Override
    public Type getComponentTypeHint() {
        // TODO get it from the typeClass.getGenericSuperclass()
        return null;
    }

    @Override
    public Object endInstance(Object instance) {
        return instance;
    }
}
