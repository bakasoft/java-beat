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

        return DefaultExaminers.createExaminer(type, null);
    }
}
