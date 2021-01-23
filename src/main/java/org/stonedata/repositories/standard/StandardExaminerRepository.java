package org.stonedata.repositories.standard;

import org.stonedata.examiners.Examiner;
import org.stonedata.examiners.ValueExaminer;
import org.stonedata.examiners.standard.array.ListExaminer;
import org.stonedata.examiners.standard.array.ArrayInstanceExaminer;
import org.stonedata.examiners.standard.object.ClassObjectExaminer;
import org.stonedata.examiners.standard.object.MapExaminer;
import org.stonedata.examiners.standard.value.ClassEnumExaminer;
import org.stonedata.examiners.standard.value.DefaultTypedValueExaminer;
import org.stonedata.examiners.standard.value.DurationExaminer;
import org.stonedata.repositories.ExaminerRepository;
import org.stonedata.types.value.DefaultTypedValue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

public class StandardExaminerRepository implements ExaminerRepository {

    private final List<ExaminerEntry> entries;

    public StandardExaminerRepository() {
        entries = new ArrayList<>();
    }

    public StandardExaminerRepository register(Examiner examiner, Class<?> type) {
        Objects.requireNonNull(examiner);
        Objects.requireNonNull(type);
        entries.add(new ExaminerEntry(examiner, type::isInstance));
        return this;
    }

    public StandardExaminerRepository register(Examiner examiner, Predicate<Object> condition) {
        Objects.requireNonNull(examiner);
        Objects.requireNonNull(condition);
        entries.add(new ExaminerEntry(examiner, condition));
        return this;
    }

    @Override
    public Examiner getExaminer(Object value) {
        for (var entry : entries) {
            if (entry.condition.test(value)) {
                return entry.examiner;
            }
        }

        if (value instanceof Duration) {
            return DurationExaminer.ANONYMOUS_INSTANCE;
        }

        return null;
    }

    private static class ExaminerEntry {
        final Examiner examiner;
        final Predicate<Object> condition;
        private ExaminerEntry(Examiner examiner, Predicate<Object> condition) {
            this.condition = condition;
            this.examiner = examiner;
        }
    }

}
