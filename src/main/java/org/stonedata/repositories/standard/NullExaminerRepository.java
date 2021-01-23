package org.stonedata.repositories.standard;

import org.stonedata.examiners.Examiner;
import org.stonedata.repositories.ExaminerRepository;

public class NullExaminerRepository implements ExaminerRepository {

    public static final NullExaminerRepository INSTANCE = new NullExaminerRepository();

    private NullExaminerRepository() {}

    @Override
    public Examiner getExaminer(Object value) {
        return null;
    }
}
