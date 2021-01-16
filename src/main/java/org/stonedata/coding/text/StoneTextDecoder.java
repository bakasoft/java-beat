package org.stonedata.coding.text;

import org.stonedata.coding.StoneCharDecoder;
import org.stonedata.errors.InvalidSyntaxException;
import org.stonedata.io.StoneCharInput;
import org.stonedata.producers.ProducerRepository;
import org.stonedata.references.ReferenceTracker;
import org.stonedata.references.impl.StandardReferenceTracker;
import org.stonedata.util.PP;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class StoneTextDecoder implements StoneCharDecoder {

    private final ReferenceTracker references;
    private final ProducerRepository producers;

    public StoneTextDecoder(ProducerRepository producers) {
        this(new StandardReferenceTracker(), producers);
    }

    public StoneTextDecoder(ReferenceTracker references, ProducerRepository producers) {
        this.references = references;
        this.producers = producers;
    }

    @Override
    public Object read(StoneCharInput input) throws IOException {
        return read(input, null);
    }

    public Object read(StoneCharInput input, Type typeHint) throws IOException {
        skipWhitespace(input);

        var c = input.peek();

        if (isTokenChar(c)) {
            return continueToHeadToken(input, typeHint);
        }
        else if(isStringDelimiter(c)) {
            return continueToHeadString(input, typeHint);
        }
        else {
            return continueWithHead(input, typeHint, null);
        }
    }

    private Object continueToHeadToken(StoneCharInput input, Type typeHint) throws IOException {
        var head = continueToken(input);

        if ("null".equals(head)) {
            return null;
        }
        else if ("true".equals(head)) {
            return true;
        }
        else if ("false".equals(head)) {
            return false;
        }
        else if (isInteger(head)) {
            return new BigInteger(head);
        }
        else if (isDecimal(head)) {
            return new BigDecimal(head);
        }
        else {
            skipWhitespace(input);

            return continueWithHead(input, typeHint, head);
        }
    }

    private Object continueToHeadString(StoneCharInput input, Type typeHint) throws IOException {
        var head = continueString(input);

        skipWhitespace(input);

        return continueWithHead(input, typeHint, head);
    }

    private Object continueWithHead(StoneCharInput input, Type typeHint, String head) throws IOException {
        var c = input.peek();

        if (c == '<') {
            return continueToReference(input, typeHint, head);
        }
        else {
            return continueWithReference(input, typeHint, head, null);
        }
    }

    private Object continueToReference(StoneCharInput input, Type typeHint, String head) throws IOException {
        input.expect('<');

        var reference = readKeyOrNull(input);

        if (reference == null) {
            throw new InvalidSyntaxException(input.getLocation(), "Expected reference key");
        }

        skipWhitespace(input);

        input.expect('>');

        skipWhitespace(input);

        return continueWithReference(input, typeHint, head, reference);
    }

    private Object continueWithReference(StoneCharInput input, Type typeHint, String head, String reference) throws IOException {
        var c = input.peek();

        if (c == '{') {
            return continueToObject(input, head, typeHint, reference);
        }
        else if (c == '[') {
            return continueToArray(input, head, typeHint, reference);
        }
        else if (c == '(') {
            return continueToValue(input, head, typeHint, reference);
        }
        else if (reference != null) {
            return references.retrieve(head, reference);
        }
        else {
            return continueWithLiteral(head, typeHint);
        }
    }

    private Object continueToObject(StoneCharInput input, String typeName, Type typeHint, String reference) throws IOException {
        var value = readObject(input, typeName, typeHint);

        if (reference != null) {
            references.store(typeName, value, reference);
        }

        return value;
    }

    private Object continueToArray(StoneCharInput input, String typeName, Type typeHint, String reference) throws IOException {
        var value = readArray(input, typeName, typeHint);

        if (reference != null) {
            references.store(typeName, value, reference);
        }

        return value;
    }

    private Object continueToValue(StoneCharInput input, String typeName, Type typeHint, String reference) throws IOException {
        var value = readValue(input, typeName, typeHint);

        if (reference != null) {
            references.store(typeName, value, reference);
        }

        return value;
    }

    private Object continueWithLiteral(String literal, Type typeHint) {
        var producer = producers.findValueProducer(null, typeHint);

        return producer.newInstance(List.of(literal));
    }

    private Object readObject(StoneCharInput input, String typeName, Type typeHint) throws IOException {
        var producer = producers.findObjectProducer(typeName, typeHint);
        var obj = producer.beginInstance();

        input.expect('{');

        do {
            skipWhitespace(input);

            var key = readKeyOrNull(input);

            if (key == null) {
                break;
            }

            skipWhitespace(input);

            input.expect(':');

            skipWhitespace(input);

            var keyTypeHint = producer.getTypeHint(key);
            var value = read(input, keyTypeHint);

            producer.set(obj, key, value);

            skipWhitespace(input);
        }
        while (input.tryPull(','));

        input.expect('}');

        return producer.endInstance(obj);
    }

    private Object readArray(StoneCharInput input, String typeName, Type typeHint) throws IOException {
        var producer = producers.findArrayProducer(typeName, typeHint);
        var componentTypeHint = producer.getComponentTypeHint();
        var arr = producer.beginInstance();

        input.expect('[');

        do {
            skipWhitespace(input);

            if (input.peek(']')) {
                break;
            }

            var value = read(input, componentTypeHint);

            producer.add(arr, value);

            skipWhitespace(input);
        }
        while (input.tryPull(','));

        input.expect(']');

        return producer.endInstance(arr);
    }

    private Object readValue(StoneCharInput input, String typeName, Type typeHint) throws IOException {
        var maker = producers.findValueProducer(typeName, typeHint);
        var arguments = new ArrayList<>();

        input.expect('(');

        do {
            skipWhitespace(input);

            if (input.peek(')')) {
                break;
            }

            var value = read(input);

            arguments.add(value);

            skipWhitespace(input);
        }
        while (input.tryPull(','));

        input.expect(')');

        return maker.newInstance(arguments);
    }

    // STATIC
    private static final Pattern INTEGER_PATTERN = Pattern.compile("[+-]?[0-9]+");

    private static final Pattern DECIMAL_PATTERN = Pattern.compile("[+-]?[0-9]+\\.[0-9]+([eE][+-][0-9]+)?");

    private static boolean isInteger(String str) {
        return INTEGER_PATTERN.matcher(str).matches();
    }

    private static boolean isDecimal(String str) {
        return DECIMAL_PATTERN.matcher(str).matches();
    }

    private static boolean isTokenChar(char c) {
        return (c >= 'a' && c <= 'z')
                || (c >= 'A' && c <= 'Z')
                || (c >= '0' && c <= '9')
                || c == '_' || c == '.' || c == '/' || c == '-' || c == '+';
    }

    private static boolean isStringDelimiter(char c) {
        return c == '\"' || c == '\'';
    }

    private static boolean isWhitespace(char c) {
        return c == ' ' || c == '\n' || c == '\t'  || c == '\r';
    }

    private static void skipWhitespace(StoneCharInput input) throws IOException {
        while (isWhitespace(input.peek())) {
            input.pull();
        }
    }

    private static String readKeyOrNull(StoneCharInput input) throws IOException {
        var c = input.peek();
        if (isTokenChar(c)) {
            return continueToken(input);
        }
        else if (isStringDelimiter(c)) {
            return continueString(input);
        }
        return null;
    }

    private static String continueToken(StoneCharInput input) throws IOException {
        var token = new StringBuilder();

        do {
            var c = input.pull();

            token.append(c);
        }
        while (isTokenChar(input.peek()));

        return token.toString();
    }

    private static String continueString(StoneCharInput input) throws IOException {
        var delimiter = input.pull();
        var buffer = new StringBuilder();

        while(input.isAlive()) {
            var chr = input.pull();

            if (chr == delimiter) {
                break;
            }
            else if (chr == '\\') {
                chr = input.pull();

                if (chr == '\\' || chr == '\"' || chr == '\'') {
                    buffer.append(chr);
                }
                else if (chr == 's') {
                    buffer.append(' ');
                }
                else if (chr == 't') {
                    buffer.append('\t');
                }
                else if (chr == 'n') {
                    buffer.append('\n');
                }
                else if (chr == 'r') {
                    buffer.append('\r');
                }
                else if (chr == 'u') {
                    var code = (char)Integer.parseInt(new String(new char[] {
                            input.pull(), input.pull(), input.pull(), input.pull(),
                    }), 16);

                    buffer.append(code);
                }
                else {
                    throw new InvalidSyntaxException(input.getLocation(),
                            String.format("Invalid escaped char: %s", PP.str(chr)));
                }
            }
            else {
                buffer.append(chr);
            }
        }

        return buffer.toString();
    }

}
