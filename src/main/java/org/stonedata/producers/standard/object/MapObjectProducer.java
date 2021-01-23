package org.stonedata.producers.standard.object;

import org.stonedata.producers.ObjectProducer;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapObjectProducer implements ObjectProducer {

    public static final MapObjectProducer INSTANCE = new MapObjectProducer();

    private MapObjectProducer() {}

    @Override
    public Object beginInstance() {
        return new LinkedHashMap<String, Object>();
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
