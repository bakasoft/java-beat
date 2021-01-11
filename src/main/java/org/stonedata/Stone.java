package org.stonedata;

import org.stonedata.examiners.ExaminerRepository;
import org.stonedata.io.StoneCharInput;
import org.stonedata.io.StoneCharOutput;
import org.stonedata.io.impl.PrettyPrintOutput;
import org.stonedata.io.impl.ReadableInput;
import org.stonedata.io.impl.SequenceInput;
import org.stonedata.io.impl.StoneAppendableOutput;
import org.stonedata.text.StoneTextEncoder;

import java.util.regex.Pattern;

public interface Stone {

    Pattern TOKEN_PATTERN = Pattern.compile("[a-zA-Z0-9_./+-]+");

    static StoneCharOutput standardOutput(Appendable output) {
        return new StoneAppendableOutput(output);
    }

    static StoneCharOutput prettyOutput(Appendable output) {
        return new PrettyPrintOutput(output);
    }

    static StoneTextEncoder standardEncoder(StoneSchema schema) {
        return standardEncoder(schema.createExaminerRepository());
    }

    static StoneTextEncoder standardEncoder(ExaminerRepository repository) {
        return new StoneTextEncoder(repository);
    }

    static StoneCharInput charInput(CharSequence sequence) {
        return new SequenceInput(sequence);
    }

    static StoneCharInput charInput(Readable readable) {
        return new ReadableInput(readable);
    }
}
