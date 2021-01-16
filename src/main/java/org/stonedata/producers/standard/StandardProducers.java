package org.stonedata.producers.standard;

import org.stonedata.producers.Producer;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class StandardProducers {

    public static Producer create(Class<?> type) {
        return create(type, null);
    }

    public static Producer create(Class<?> type, String nameHint) {
        if (type.isArray()) {
            return StandardArrayProducers.create(type, nameHint);
        }
        else if (type.isEnum()) {
            return StandardValueProducers.create(type, nameHint);
        }
        else if (List.class.isAssignableFrom(type)) {
            return StandardArrayProducers.create(type, nameHint);
        }
        else if (Map.class.isAssignableFrom(type)) {
            return StandardObjectProducers.create(type, nameHint);
        }
        else if (type == Duration.class) {
            return StandardValueProducers.create(type, nameHint);
        }
        return StandardObjectProducers.create(type, nameHint);
    }
}
