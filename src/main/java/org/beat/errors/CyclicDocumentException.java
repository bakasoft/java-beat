package org.beat.errors;

import org.beat.examiners.Examiner;
import org.beat.util.PP;

public class CyclicDocumentException extends BeatException {

    public CyclicDocumentException(Object value, Examiner examiner) {
        this(generateMessage(value, examiner));
    }

    public CyclicDocumentException(String merssage) {
        super(merssage);
    }

    private static String generateMessage(Object value, Examiner examiner) {
        var examinerType = examiner.getTypeName() != null ? PP.str(examiner.getTypeName()) : PP.typeOf(examiner);
        var valueType = PP.typeOf(value);


        return String.format("Cyclic data detected while examining %s with %s.", valueType, examinerType);
    }
}
