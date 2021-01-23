package org.stonedata.producers.standard.value;

import org.stonedata.errors.StoneException;
import org.stonedata.producers.ValueProducer;

import java.time.Duration;

public class DurationProducer implements ValueProducer {

    public static final DurationProducer INSTANCE = new DurationProducer();

    private DurationProducer() {}

    @Override
    public Object newInstance(Object[] arguments) {
        if (arguments.length == 0) {
            return 0;
        }
        else if (arguments.length == 1) {
            var value = String.valueOf(arguments[0]);

            return Duration.parse(value);
        }
        else {
            throw new StoneException("Expected one argument.");
        }
    }
}
