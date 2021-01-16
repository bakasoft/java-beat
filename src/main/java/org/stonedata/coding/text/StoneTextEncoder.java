package org.stonedata.coding.text;

import org.stonedata.errors.CyclicDocumentException;
import org.stonedata.examiners.ArrayExaminer;
import org.stonedata.examiners.Examiner;
import org.stonedata.repositories.ExaminerRepository;
import org.stonedata.examiners.ObjectExaminer;
import org.stonedata.examiners.ValueExaminer;
import org.stonedata.references.ReferenceProvider;
import org.stonedata.errors.StoneException;
import org.stonedata.io.StoneCharOutput;
import org.stonedata.coding.StoneCharEncoder;
import org.stonedata.references.impl.NullReferenceProvider;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public class StoneTextEncoder implements StoneCharEncoder {

    private final ReferenceProvider references;
    private final ExaminerRepository examiners;

    private boolean skipNullFields;

    private final Deque<Object> stack;

    public StoneTextEncoder(ExaminerRepository examiners) {
        this(examiners, NullReferenceProvider.INSTANCE);
    }

    public StoneTextEncoder(ExaminerRepository examiners, ReferenceProvider references) {
        this.examiners = examiners;
        this.references = references;
        this.stack = new ArrayDeque<>();
    }

    @Override
    public void write(StoneCharOutput output, Object value) throws IOException {
        write(output, new HashSet<>(), value);
    }

    public void write(StoneCharOutput output, Set<Object> writtenRefs, Object value) throws IOException {
        var examiner = examiners.getExaminerFor(value);

        if (examiner == null) {
            var reference = references.getReference(null, value);

            if (reference != null) {
                writeStandardValueWithReference(output, writtenRefs, value, reference);
            }
            else {
                writeStandardValue(output, value);
            }
        }
        else {
            var typeName = examiner.getType();
            var reference = references.getReference(typeName, value);

            if (typeName == null) {
                if (reference == null) {
                    writeExamined(output, writtenRefs, value, examiner);
                }
                else {
                    writeExaminedWithReference(output, writtenRefs, value, examiner, reference);
                }
            }
            else if (reference == null) {
                writeExaminedWithTypeName(output, writtenRefs, value, examiner, typeName);
            }
            else {
                writeExaminedWithTypeNameAndReference(output, writtenRefs, value, examiner, typeName, reference);
            }
        }
    }

    private void writeExaminedWithTypeNameAndReference(StoneCharOutput output, Set<Object> writtenRefs, Object value, Examiner examiner, String typeName, String reference) throws IOException {
        writeString(output, typeName);
        output.write('<');
        writeString(output, reference);
        output.write('>');
        if (writtenRefs.add(reference + "\0" + typeName)) {
            output.space();
            writeExamined(output, writtenRefs, value, examiner);
        }
    }

    private void writeExaminedWithTypeName(StoneCharOutput output, Set<Object> writtenRefs, Object value, Examiner examiner, String typeName) throws IOException {
        writeString(output, typeName);
        output.space();
        writeExamined(output, writtenRefs, value, examiner);
    }

    private void writeExaminedWithReference(StoneCharOutput output, Set<Object> writtenRefs, Object value, Examiner examiner, String reference) throws IOException {
        output.write('<');
        writeString(output, reference);
        output.write('>');
        if (writtenRefs.add(reference + "\0")) {
            output.space();
            writeExamined(output, writtenRefs, value, examiner);
        }
    }

    private void writeExamined(StoneCharOutput output, Set<Object> writtenRefs, Object value, Examiner examiner) throws IOException {
        if (stack.contains(value)) {
            throw new CyclicDocumentException(value, examiner);
        }

        stack.push(value);

        if (examiner instanceof ObjectExaminer) {
            writeObject(output, writtenRefs, value, (ObjectExaminer) examiner);
        }
        else if (examiner instanceof ArrayExaminer) {
            writeArray(output, writtenRefs, value, (ArrayExaminer) examiner);
        }
        else if (examiner instanceof ValueExaminer) {
            writeValue(output, writtenRefs, value, (ValueExaminer) examiner);
        }
        else {
            throw new StoneException();
        }

        stack.pop();
    }

    private void writeStandardValueWithReference(StoneCharOutput output, Set<Object> writtenRefs, Object value, String reference) throws IOException {
        output.write('<');
        writeString(output, reference);
        output.write('>');
        if (writtenRefs.add(reference + "\0")) {
            output.space();
            output.write('(');
            writeStandardValue(output, value);
            output.write(')');
        }
    }

    private void writeStandardValue(StoneCharOutput output, Object value) throws IOException {
        if (value == null) {
            writeNull(output);
        }
        else if (value instanceof String) {
            writeString(output, (String)value);
        }
        else if (value instanceof Boolean) {
            writeBoolean((Boolean)value, output);
        }
        else if (value instanceof Number) {
            writeNumber((Number)value, output);
        }
        else if (value instanceof Character) {
            writeChar((Character)value, output);
        }
        else {
            throw new StoneException();
        }
    }

    private void writeObject(StoneCharOutput output, Set<Object> writtenRefs, Object value, ObjectExaminer examiner) throws IOException {
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

            if (entryValue != null || !skipNullFields) {
                if (i > 0) {
                    output.write(',');
                    output.line();
                }

                writeString(output, entryKey);

                output.write(':');
                output.space();

                write(output, writtenRefs, entryValue);
                i++;
            }
        }

        output.indent(-1);
        output.line();
        output.write('}');
    }

    private void writeArray(StoneCharOutput output, Set<Object> writtenRefs, Object value, ArrayExaminer examiner) throws IOException {
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

            write(output, writtenRefs, item);
        }

        output.indent(-1);
        output.line();
        output.write(']');
    }

    private void writeValue(StoneCharOutput output, Set<Object> writtenRefs, Object value, ValueExaminer examiner) throws IOException {
        var args = examiner.computeArguments(value);

        output.write('(');

        for (var i = 0; i < args.size(); i++) {
            if (i > 0) {
                output.write(',');
                output.space();
            }

            write(output, writtenRefs, args.get(i));
        }

        output.write(')');
}

    private static void writeChar(char value, StoneCharOutput output) throws IOException {
        output.write('"');

        writeUnquotedChar(value, output);

        output.write('"');
    }

    private static void writeNumber(Number value, StoneCharOutput output) throws IOException {
        output.write(String.valueOf(value));
    }

    private static void writeBoolean(boolean value, StoneCharOutput output) throws IOException {
        output.write(value ? "true" : "false");
    }

    private static void writeString(StoneCharOutput output, String value) throws IOException {
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

    private static void writeNull(StoneCharOutput output) throws IOException {
        output.write("null");
    }

    private static void writeUnquotedChar(Character chr, StoneCharOutput output) throws IOException {
        if (chr == '\"' || chr == '\\') {
            output.write('\\');
            output.write(chr);
        }
        else if (chr == '\t') {
            output.write("\\t");
        }
        else if (chr == '\r') {
            output.write("\\r");
        }
        else if (chr == '\n') {
            output.write("\\n");
        }
        // TODO \\uHHHH
        else {
            output.write(chr);
        }
    }

    public boolean isSkipNullFields() {
        return skipNullFields;
    }

    public void setSkipNullFields(boolean skipNullFields) {
        this.skipNullFields = skipNullFields;
    }
}
