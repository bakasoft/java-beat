package org.stonedata.examiners;

public interface ValueExaminer extends Examiner {
    Object extractArgument(Object value);
}
