package org.beat.errors;

import org.beat.util.StringUtils;

import java.lang.reflect.Type;

public class ProducerNotFoundException extends BeatException {
    public ProducerNotFoundException(String message) {
        super(message);
    }

    public ProducerNotFoundException(String target, String type, Type typeHint) {
        super(generateMessage(target, type, typeHint));
    }

    private static String generateMessage(String target, String type, Type typeHint) {
        if (StringUtils.isEmpty(type)) {
            if (typeHint != null) {
                return String.format("Missing default %s producer (%s).", target, typeHint);
            }
            else {
                return String.format("Missing default %s producer.", target);
            }
        }
        else if (typeHint == null) {
            return String.format("Missing %s producer for %s.", target, type);
        }
        else {
            return String.format("Missing %s producer for %s (%s).", target, type, typeHint);
        }
    }
}
