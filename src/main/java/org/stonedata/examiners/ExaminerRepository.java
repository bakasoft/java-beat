package org.stonedata.examiners;

public interface ExaminerRepository {
    Examiner findExaminer(Class<?> type);  // TODO change to value?
}
