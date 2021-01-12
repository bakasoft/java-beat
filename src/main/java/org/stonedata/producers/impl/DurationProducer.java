package org.stonedata.producers.impl;

import org.stonedata.errors.StoneException;
import org.stonedata.producers.ValueProducer;

import java.time.Duration;
import java.util.List;

public class DurationProducer implements ValueProducer {
    @Override
    public Object newInstance(String type, List<?> arguments) {
        if (arguments.isEmpty()) {
            return 0;
        }
        else if (arguments.size() == 1) {
            var value = String.valueOf(arguments.get(0));
            return Duration.parse(value);
        }
        else {
            throw new StoneException("Expected one argument.");
        }
    }
}
