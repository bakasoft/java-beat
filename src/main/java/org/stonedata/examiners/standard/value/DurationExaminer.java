package org.stonedata.examiners.standard.value;

import org.stonedata.examiners.ValueExaminer;

import java.time.Duration;
import java.util.List;

public class DurationExaminer implements ValueExaminer {

    private final String type;

    public DurationExaminer(String type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public List<Object> computeArguments(Object value) {
        var duration = (Duration)value;

        return List.of(duration.toString());
    }
}
