package org.stonedata.examiners;

import java.util.Set;

public interface ObjectExaminer extends Examiner {
    Set<String> getKeys(Object value);

    // TODO add get type of key?

    Object getValue(Object value, String key);
}
