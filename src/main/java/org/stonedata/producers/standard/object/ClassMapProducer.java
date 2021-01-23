package org.stonedata.producers.standard.object;

import org.stonedata.producers.ObjectProducer;
import org.stonedata.util.ReflectUtils;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.Supplier;

public class ClassMapProducer implements ObjectProducer {

    private final Supplier<?> mapMaker;

    public ClassMapProducer(Class<?> typeClass) {
        mapMaker = ReflectUtils.extractEmptyConstructor(typeClass, Map.class);
    }

    @Override
    public Object beginInstance() {
        return mapMaker.get();
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> restoreMap(Object instance) {
        return (Map<String, Object>)instance;
    }

    @Override
    public void set(Object instance, String key, Object value) {
        var map = restoreMap(instance);

        map.put(key, value);
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
