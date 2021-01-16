package org.stonedata.producers.standard;

import org.stonedata.producers.Producer;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class StandardProducers {

    public static Producer create(Class<?> type) {
        if (type.isArray()) {
            return StandardArrayProducers.tryCreate(type);
        }
        else if (type.isEnum()) {
            return StandardValueProducers.tryCreate(type);
        }
        else if (List.class.isAssignableFrom(type)) {
            return StandardArrayProducers.tryCreate(type);
        }
        else if (Map.class.isAssignableFrom(type)) {
            return StandardObjectProducers.tryCreate(type);
        }
        else if (type == Duration.class) {
            return StandardValueProducers.tryCreate(type);
        }
        return StandardObjectProducers.tryCreate(type);
    }
}
