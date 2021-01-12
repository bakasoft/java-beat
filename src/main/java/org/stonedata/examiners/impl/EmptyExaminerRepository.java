package org.stonedata.examiners.impl;

import org.stonedata.errors.ExaminerNotFoundException;
import org.stonedata.errors.StoneException;
import org.stonedata.examiners.Examiner;
import org.stonedata.examiners.ExaminerRepository;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class EmptyExaminerRepository implements ExaminerRepository {
    @Override
    public Examiner findExaminer(Class<?> type) {
        if (type.isArray()) {
            return GenericArrayExaminer.INSTANCE;  // Any array
        }
        else if (List.class.isAssignableFrom(type)) {
            return GenericListExaminer.ANONYMOUS_INSTANCE;  // Any List
        }
        else if (Map.class.isAssignableFrom(type)) {
            return GenericMapExaminer.ANONYMOUS_INSTANCE;  // Any List
        }
        else if (type == Duration.class) {
            return new DurationExaminer(null);
        }

        return new GenericObjectExaminer(type, null);
    }
}
