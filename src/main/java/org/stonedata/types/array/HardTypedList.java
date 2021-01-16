package org.stonedata.types.array;

import java.util.ArrayList;

public class HardTypedList<T> extends ArrayList<T> {

    private final Class<T> componentType;

    public HardTypedList(Class<T> componentType) {
        this.componentType = componentType;
    }

    public Class<T> getComponentType() {
        return componentType;
    }
}
