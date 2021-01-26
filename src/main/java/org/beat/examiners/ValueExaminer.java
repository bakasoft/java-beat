package org.beat.examiners;

public interface ValueExaminer extends Examiner {
    Object extractArgument(Object value);
}
