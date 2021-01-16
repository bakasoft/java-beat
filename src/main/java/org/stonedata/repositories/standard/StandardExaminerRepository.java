package org.stonedata.repositories.standard;

import org.stonedata.examiners.Examiner;
import org.stonedata.repositories.ExaminerRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class StandardExaminerRepository implements ExaminerRepository {

    private Map<Object, Examiner> valueExaminers;
    private List<TestExaminer> testExaminers;

    public void registerForValue(Examiner examiner, Object value) {
        if (valueExaminers == null) {
            valueExaminers = new HashMap<>();
        }
        valueExaminers.put(value, examiner);
    }

    public void registerForCondition(Examiner examiner, Predicate<Object> condition) {
        if (testExaminers == null) {
            testExaminers = new ArrayList<>();
        }
        testExaminers.add(new TestExaminer(condition, examiner));
    }

    @Override
    public Examiner getExaminerFor(Object value) {
        if (valueExaminers != null) {
            var examiner = valueExaminers.get(value);

            if (examiner != null) {
                return examiner;
            }
        }
        if (testExaminers != null) {
            for (var testExaminer : testExaminers) {
                if (testExaminer.condition.test(value)) {
                    return testExaminer.examiner;
                }
            }
        }
        return null;
    }

    private static class TestExaminer {
        public final Predicate<Object> condition;
        public final Examiner examiner;
        private TestExaminer(Predicate<Object> condition, Examiner examiner) {
            this.condition = condition;
            this.examiner = examiner;
        }
    }
}
