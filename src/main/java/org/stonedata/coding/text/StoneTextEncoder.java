package org.stonedata.coding.text;

import org.stonedata.examiners.ArrayExaminer;
import org.stonedata.examiners.ExaminerRepository;
import org.stonedata.examiners.ObjectExaminer;
import org.stonedata.examiners.ValueExaminer;
import org.stonedata.references.StoneReferenceStore;
import org.stonedata.errors.StoneException;
import org.stonedata.io.StoneCharOutput;
import org.stonedata.coding.StoneCharEncoder;
import org.stonedata.references.impl.StandardReferenceStore;

import java.io.IOException;

public class StoneTextEncoder implements StoneCharEncoder {

    private final StoneReferenceStore references;
    private final ExaminerRepository examiners;
    private boolean useStandardExaminers;

    public StoneTextEncoder(ExaminerRepository examiners) {
        this(examiners, new StandardReferenceStore());
    }

    public StoneTextEncoder(ExaminerRepository examiners, StoneReferenceStore references) {
        this.examiners = examiners;
        this.references = references;
        this.useStandardExaminers = true;
    }

    public void write(Object value, StoneCharOutput output) throws IOException {
        if (useStandardExaminers && tryWriteStandardValue(value, output)) {
            return;
        }

        var examiner = examiners.findExaminer(value.getClass());
        var type = examiner.getType();

        if (type != null) {
            var reference = references.getReference(type, value);
            var onlyReference = (reference != null);

            if (reference == null) {
                reference = references.add(type, value);
            }

            writeString(type, output);
            output.write('<');
            write(reference, output);
            output.write('>');

            if (onlyReference) {
                return;
            }

            output.space();
        }

        if (examiner instanceof ObjectExaminer) {
            writeObject(value, (ObjectExaminer)examiner, output);
        }
        else if (examiner instanceof ArrayExaminer) {
            writeArray(value, (ArrayExaminer)examiner, output);
        }
        else if (examiner instanceof ValueExaminer) {
            writeValue(value, (ValueExaminer)examiner, output);
        }
        else {
            throw new StoneException();
        }
    }

    private boolean tryWriteStandardValue(Object value, StoneCharOutput output) throws IOException {
        if (value == null) {
            writeNull(output);
            return true;
        }
        else if (value instanceof String) {
            writeString((String)value, output);
            return true;
        }
        else if (value instanceof Boolean) {
            writeBoolean((Boolean)value, output);
            return true;
        }
        else if (value instanceof Number) {
            writeNumber((Number)value, output);
            return true;
        }
        else if (value instanceof Character) {
            writeChar((Character)value, output);
            return true;
        }
        return false;
    }

    private void writeObject(Object value, ObjectExaminer examiner, StoneCharOutput output) throws IOException {
        var entryKeys = examiner.getKeys(value);

        if (entryKeys.isEmpty()) {
            output.write("{}");
            return;
        }

        output.write('{');
        output.indent(+1);
        output.line();

        var i = 0;
        for (var entryKey : entryKeys) {
            var entryValue = examiner.getValue(value, entryKey);

            if (i > 0) {
                output.write(',');
                output.line();
            }

            writeString(entryKey, output);

            output.write(':');
            output.space();

            write(entryValue, output);
            i++;
        }

        output.indent(-1);
        output.line();
        output.write('}');
    }

    private void writeArray(Object value, ArrayExaminer examiner, StoneCharOutput output) throws IOException {
        var size = examiner.getSizeOf(value);

        if (size == 0) {
            output.write("[]");
            return;
        }

        output.write('[');
        output.indent(+1);
        output.line();

        for (var i = 0; i < size; i++) {
            var item = examiner.getValueAt(i, value);

            if (i > 0) {
                output.write(',');
                output.line();
            }

            write(item, output);
        }

        output.indent(-1);
        output.line();
        output.write(']');
    }

    private void writeValue(Object value, ValueExaminer examiner, StoneCharOutput output) throws IOException {
        var args = examiner.computeArguments(value);

        if (args.size() == 1) {
            write(args.get(0), output);
        }
        else {
            output.write('(');

            for (var i = 0; i < args.size(); i++) {
                if (i > 0) {
                    output.write(',');
                    output.space();
                }

                write(args.get(i), output);
            }

            output.write(')');
        }
    }

    private void writeChar(char value, StoneCharOutput output) throws IOException {
        output.write('"');

        writeUnquotedChar(value, output);

        output.write('"');
    }

    private void writeNumber(Number value, StoneCharOutput output) throws IOException {
        output.write(String.valueOf(value));
    }

    private void writeBoolean(boolean value, StoneCharOutput output) throws IOException {
        output.write(value ? "true" : "false");
    }

    private void writeString(String value, StoneCharOutput output) throws IOException {
        if (value.matches("[a-zA-Z0-9_./+-]+")) {
            output.write(value);
        }
        else {
            var length = value.length();

            output.write('"');

            for (var i = 0; i < length; i++) {
                var chr = value.charAt(i);

                writeUnquotedChar(chr, output);
            }

            output.write('"');
        }
    }

    private void writeNull(StoneCharOutput output) throws IOException {
        output.write("null");
    }

    private static void writeUnquotedChar(Character chr, StoneCharOutput output) throws IOException {
        if (chr == '\"' || chr == '\\') {
            output.write('\\');
            output.write(chr);
        }
        // TODO add escaping
        else {
            output.write(chr);
        }
    }


}
