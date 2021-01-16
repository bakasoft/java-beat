package org.stonedata.repositories;

import org.stonedata.examiners.Examiner;

public interface ExaminerRepository {
    Examiner getExaminerFor(Object value);
}
