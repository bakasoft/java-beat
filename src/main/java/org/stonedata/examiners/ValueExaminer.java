package org.stonedata.examiners;

import java.util.List;

public interface ValueExaminer extends Examiner {
    List<Object> computeArguments(Object value);
}