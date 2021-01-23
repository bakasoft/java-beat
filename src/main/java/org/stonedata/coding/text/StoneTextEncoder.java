package org.stonedata.coding.text;

import org.stonedata.errors.CyclicDocumentException;
import org.stonedata.errors.UnsupportedValueException;
import org.stonedata.examiners.ArrayExaminer;
import org.stonedata.examiners.Examiner;
import org.stonedata.examiners.standard.array.ArrayInstanceExaminer;
import org.stonedata.examiners.standard.array.ListExaminer;
import org.stonedata.examiners.standard.object.ClassObjectExaminer;
import org.stonedata.examiners.standard.object.MapExaminer;
import org.stonedata.examiners.standard.value.ClassEnumExaminer;
import org.stonedata.examiners.standard.value.DefaultTypedValueExaminer;
import org.stonedata.examiners.standard.value.ValueIdentityExaminer;
import org.stonedata.io.impl.AppendableOutput;
import org.stonedata.repositories.ExaminerRepository;
import org.stonedata.examiners.ObjectExaminer;
import org.stonedata.examiners.ValueExaminer;
import org.stonedata.references.ReferenceProvider;
import org.stonedata.io.StoneCharOutput;
import org.stonedata.types.array.DefaultTypedList;
import org.stonedata.types.object.DefaultTypedObject;
import org.stonedata.types.value.DefaultTypedValue;
import org.stonedata.util.PP;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StoneTextEncoder {

    private final ReferenceProvider references;
    private final ExaminerRepository examiners;

    private boolean skipNullFields;

    private final Deque<Object> cycleStack;

    public StoneTextEncoder() {
        this(null, null);
    }

    public StoneTextEncoder(ExaminerRepository examiners) {
        this(examiners, null);
    }

    public StoneTextEncoder(ReferenceProvider references) {
        this(null, references);
    }

    public StoneTextEncoder(ExaminerRepository examiners, ReferenceProvider references) {
        this.examiners = examiners;
        this.references = references;
        this.cycleStack = new ArrayDeque<>();
    }

    public boolean isSkipNullFields() {
        return skipNullFields;
    }

    public void setSkipNullFields(boolean skipNullFields) {
        this.skipNullFields = skipNullFields;
    }

    public String write(Object value) {
        var buffer = new StringBuilder();
        var output = new AppendableOutput(buffer);

        write(value, output);

        return buffer.toString();
    }

    public void write(Object value, StoneCharOutput output) {
        write(output, new HashSet<>(), value);
    }

    private void write(StoneCharOutput output, Set<Object> writtenRefs, Object value) {
        var reference = references != null ? references.getReference(value) : null;
        if (reference == null) {
            var examiner = searchExaminer(value);
            var typeName = examiner.getType();
            if (typeName == null) {
                writeContent(output, writtenRefs, value, examiner, false);
            }
            else {
                writeString(output, typeName);
                output.space();
                writeContent(output, writtenRefs, value, examiner, true);
            }
        }
        else if (writtenRefs.add(reference)) {
            var examiner = searchExaminer(value);
            var typeName = examiner.getType();
            if (typeName != null) {
                writeString(output, typeName);
            }
            writeReference(output, reference);
            output.space();
            writeContent(output, writtenRefs, value, examiner, true);
        }
        else {
            writeReference(output, reference);
        }
    }

    private Examiner searchExaminer(Object value) {
        // #1: Search in repository
        if (examiners != null) {
            var examiner = examiners.getExaminer(value);

            if (examiner != null) {
                return examiner;
            }
        }

        // TODO: Move to a module?

        // #2: Search by instance
        if (value == null
                || value instanceof String
                || value instanceof Boolean
                || value instanceof Number
                || value instanceof Character) {
            return ValueIdentityExaminer.INSTANCE;
        }
        else if (value instanceof DefaultTypedObject) {
            var typeName = ((DefaultTypedObject)value).getTypeName();
            if (typeName == null) {
                return MapExaminer.ANONYMOUS_INSTANCE;
            }
            return new MapExaminer(typeName);
        }
        else if (value instanceof DefaultTypedList) {
            var typeName = ((DefaultTypedList)value).getTypeName();
            if (typeName == null) {
                return ListExaminer.ANONYMOUS_INSTANCE;
            }
            return new ListExaminer(typeName);
        }
        else if (value instanceof DefaultTypedValue) {
            var typeName = ((DefaultTypedValue)value).getTypeName();
            if (typeName == null) {
                return ValueIdentityExaminer.INSTANCE;
            }
            return new DefaultTypedValueExaminer(typeName);
        }
        else if (value instanceof Map) {
            return MapExaminer.ANONYMOUS_INSTANCE;
        }
        else if (value instanceof List) {
            return ListExaminer.ANONYMOUS_INSTANCE;
        }

        // #3: Search by type
        var typeClass = value.getClass();
        if (typeClass.isArray()) {
            return ArrayInstanceExaminer.ANONYMOUS_INSTANCE;
        }
        else if (typeClass.isEnum()) {
            return new ClassEnumExaminer(typeClass, null);
        }
        return new ClassObjectExaminer(typeClass, null);
    }

    private void writeContent(StoneCharOutput output, Set<Object> writtenRefs, Object value, Examiner examiner, boolean wrap) {
        if (examiner instanceof ValueExaminer) {
            writeValue(output, writtenRefs, value, (ValueExaminer) examiner, wrap);
        }
        else {
            if (cycleStack.contains(value)) {
                throw new CyclicDocumentException(value, examiner);
            }

            cycleStack.push(value);

            if (examiner instanceof ObjectExaminer) {
                writeObject(output, writtenRefs, value, (ObjectExaminer) examiner);
            }
            else if (examiner instanceof ArrayExaminer) {
                writeArray(output, writtenRefs, value, (ArrayExaminer) examiner);
            }
            else {
                throw new UnsupportedValueException("Unsupported examiner: " + PP.typeOf(examiner));
            }

            cycleStack.pop();
        }
    }

    private void writeObject(StoneCharOutput output, Set<Object> writtenRefs, Object value, ObjectExaminer examiner) {
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

    private void writeArray(StoneCharOutput output, Set<Object> writtenRefs, Object value, ArrayExaminer examiner) {
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

    private void writeValue(StoneCharOutput output, Set<Object> writtenRefs, Object value, ValueExaminer examiner, boolean wrap) {
        var argument = examiner.extractArgument(value);

        if (argument == null) {
            writeNull(output, wrap);
        }
        else if (argument instanceof String) {
            writeString(output, (String) argument, wrap);
        }
        else if (argument instanceof Boolean) {
            writeBoolean((Boolean) argument, output, wrap);
        }
        else if (argument instanceof Number) {
            writeNumber((Number) argument, output, wrap);
        }
        else if (argument instanceof Character) {
            writeChar((Character) argument, output, wrap);
        }
        else if (argument instanceof List) {
            writeArguments(output, writtenRefs, (List<?>)argument, wrap);
        } else {
            throw new UnsupportedValueException("Unsupported value: " + PP.typeOf(value));
        }
    }

    private void writeArguments(StoneCharOutput output, Set<Object> writtenRefs, List<?> args, boolean wrap) {
        if (args.size() != 1 || wrap) {
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
        else {
            write(output, writtenRefs, args.get(0));
        }
    }

    private static void writeReference(StoneCharOutput output, String reference) {
        output.write('<');
        writeString(output, reference);
        output.write('>');
    }

    private static void writeChar(char value, StoneCharOutput output, boolean wrap) {
        if (wrap) {
            output.write("(\"");
            writeUnquotedChar(value, output);
            output.write("\")");
        }
        else {
            output.write('"');
            writeUnquotedChar(value, output);
            output.write('"');
        }
    }

    private static void writeNumber(Number value, StoneCharOutput output, boolean wrap) {
        var code = String.valueOf(value);
        if (wrap) {
            output.write('(');
            output.write(code);
            output.write(')');
        }
        else {
            output.write(code);
        }
    }

    private static void writeBoolean(boolean value, StoneCharOutput output, boolean wrap) {
        if (wrap) {
            output.write(value ? "(true)" : "(false)");
        }
        else {
            output.write(value ? "true" : "false");
        }
    }

    private static void writeString(StoneCharOutput output, String value, boolean wrap) {
        if (wrap) {
            output.write('(');
            writeString(output, value);
            output.write(')');
        }
        else {
            writeString(output, value);
        }
    }

    private static void writeString(StoneCharOutput output, String value) {
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

    private static void writeNull(StoneCharOutput output, boolean wrap) {
        if (wrap) {
            output.write("(null)");
        }
        else {
            output.write("null");
        }
    }

    private static void writeUnquotedChar(Character chr, StoneCharOutput output) {
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
}
