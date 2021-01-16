package org.stonedata.types.object;

import org.stonedata.errors.StoneException;

import java.util.LinkedHashMap;

public class SoftTypedObject extends LinkedHashMap<String, Object> {

    private final String type;

    public SoftTypedObject(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void set(String key, Object value) {
        if (containsKey(key)) {
            throw new StoneException();
        }
        put(key, value);
    }
}
