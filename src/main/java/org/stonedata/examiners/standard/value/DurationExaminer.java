package org.stonedata.examiners.standard.value;

import org.stonedata.examiners.ValueExaminer;

import java.time.Duration;

public class DurationExaminer implements ValueExaminer {

    public static final DurationExaminer ANONYMOUS_INSTANCE = new DurationExaminer(null);

    private final String type;

    public DurationExaminer() {
        this(null);
    }

    public DurationExaminer(String type) {
        this.type = type;
    }

    @Override
    public String getTypeName() {
        return type;
    }

    @Override
    public Object extractArgument(Object value) {
        var duration = (Duration)value;

        return duration.toString();
    }
}
