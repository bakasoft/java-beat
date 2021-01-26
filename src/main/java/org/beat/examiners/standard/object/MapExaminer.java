package org.beat.examiners.standard.object;

import org.beat.errors.BeatException;
import org.beat.examiners.ObjectExaminer;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class MapExaminer implements ObjectExaminer {

    public static final MapExaminer ANONYMOUS_INSTANCE = new MapExaminer(null);

    private final String type;

    public MapExaminer(String type) {
        this.type = type;
    }

    private static Map<?,?> map(Object any) {
        return (Map<?,?>)any;
    }

    @Override
    public String getTypeName() {
        return type;
    }

    @Override
    public Set<String> getKeys(Object value) {
        var keys = new LinkedHashSet<String>();

        for (var key : map(value).keySet()) {
            if (key instanceof String) {
                keys.add(((String) key));
            }
            else {
                throw new BeatException();
            }
        }

        return keys;
    }

    @Override
    public Object getValue(Object value, String key) {
        return map(value).get(key);
    }
}
