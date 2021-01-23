package org.stonedata.errors;

import org.stonedata.examiners.Examiner;
import org.stonedata.util.PP;

public class CyclicDocumentException extends StoneException {

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
