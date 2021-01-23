package org.stonedata.producers.standard.array;

import org.stonedata.errors.StoneException;
import org.stonedata.producers.ArrayProducer;
import org.stonedata.util.ReflectUtils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Supplier;

public class ClassListProducer implements ArrayProducer {
    private final Supplier<?> listMaker;

    public ClassListProducer(Class<?> typeClass) {
        listMaker = ReflectUtils.extractEmptyConstructor(typeClass, List.class);
    }

    @Override
    public Object beginInstance() {
        return listMaker.get();
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
