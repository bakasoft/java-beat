package org.stonedata.examiners.impl;

import org.stonedata.Stone;
import org.stonedata.examiners.Examiner;
import org.stonedata.errors.StoneException;
import org.stonedata.examiners.ExaminerRepository;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class StandardExaminerRepository implements ExaminerRepository {

    private final Stone stone;

    public StandardExaminerRepository(Stone stone) {
        this.stone = stone;
    }

    @Override
    public Examiner findExaminer(Class<?> type) {
        var examiner = stone.getExaminer(type);

        if (examiner != null) {
            return examiner;  // Best scenario
        }
        else if (type.isArray()) {
            return ObjectArrayExaminer.INSTANCE;  // Any array
        }
        else if (List.class.isAssignableFrom(type)) {
            return ListExaminer.ANONYMOUS_INSTANCE;  // Any List
        }
        else if (Map.class.isAssignableFrom(type)) {
            return MapExaminer.ANONYMOUS_INSTANCE;  // Any List
        }
        else if (type == Duration.class) {
            return new DurationExaminer(null);
        }

        return new ClassObjectExaminer(type, null);
    }
}
