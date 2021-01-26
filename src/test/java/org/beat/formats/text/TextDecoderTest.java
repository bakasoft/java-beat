package org.beat.formats.text;

import org.junit.jupiter.api.Test;
import org.beat.errors.InvalidSyntaxException;
import org.beat.io.standard.SequenceInput;
import org.beat.producers.ValueProducer;
import org.beat.producers.standard.array.ClassListProducer;
import org.beat.producers.standard.object.ClassObjectProducer;
import org.beat.producers.standard.value.DurationProducer;
import org.beat.references.impl.StandardReferenceTracker;
import org.beat.repositories.standard.StandardProducerRepository;
import org.beat.types.DefaultTypedList;
import org.beat.types.DefaultTypedObject;
import org.beat.types.DefaultObject;
import org.beat.types.DefaultList;
import org.beat.types.DefaultTypedValue;
import org.beat.types.DefaultValue;
import org.beat.util.PP;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.CustomAssertions.assertException;
import static util.CustomAssertions.assertInstanceOf;
import static util.CustomAssertions.assertNotInstanceOf;

class TextDecoderTest {

    public static class Message {
        public String content;
    }

    public static class StringList extends ArrayList<String> {}

    @Test
    void testGetReference() {
        var references = new StandardReferenceTracker();
        var value = new Object();
        references.store("1", value);

        var decoder = new TextDecoder(references);
        var result = decoder.read("<1>");

        assertSame(value, result);
    }

    @Test
    void testPutReference() {
        var references = new StandardReferenceTracker();
        var decoder = new TextDecoder(references);
        var obj = decoder.read("<1>{}");
        var arr = decoder.read("<2>[]");
        var val = decoder.read("<3>()");

        assertInstanceOf(DefaultObject.class, references.retrieve("1"));
        assertInstanceOf(DefaultList.class, references.retrieve("2"));
        assertInstanceOf(DefaultValue.class, references.retrieve("3"));
    }

    @Test
    void testReadLiteralValue() {
        var decoder = new TextDecoder();

        assertNull(decoder.read("null"));
        assertTrue(decoder.read("true", Boolean.class));
        assertFalse(decoder.read("false", Boolean.class));
        assertEquals("abc", decoder.read("\"abc\""));
        assertEquals("abc", decoder.read("abc"));
        assertInstanceOf(Number.class, decoder.read("0"));
        assertInstanceOf(Number.class, decoder.read("0.5"));
    }

    @Test
    void testReadUnwrappedValueCustomProducer() {
        var repository = new StandardProducerRepository();
        var decoder = new TextDecoder(repository);

        repository.register(Boolean.class, ValueProducer.of("on"::equals));

        assertTrue(decoder.read("on", Boolean.class));
        assertFalse(decoder.read("off", Boolean.class));
    }

    @Test
    void testReadWrappedValueDefaultProducer() {
        var decoder = new TextDecoder();

        assertInstanceOf(DefaultValue.class, decoder.read("()"));
        assertInstanceOf(DefaultTypedValue.class, decoder.read("T()"));
        assertEquals(Map.of(), decoder.read("({})"));
    }

    @Test
    void testReadValueCustomProducerByName() {
        var repository = new StandardProducerRepository();
        var decoder = new TextDecoder(repository);
        repository.register("D", DurationProducer.INSTANCE);

        var result = decoder.read("D(PT1S)");

        assertInstanceOf(Duration.class, result);
        assertEquals(1, ((Duration)result).get(ChronoUnit.SECONDS));
    }

    @Test
    void testReadValueCustomProducerByHint() {
        var repository = new StandardProducerRepository();
        var decoder = new TextDecoder(repository);
        repository.register(Duration.class, DurationProducer.INSTANCE);

        var result = decoder.read("PT1S", Duration.class);

        assertEquals(1, result.get(ChronoUnit.SECONDS));
    }

    @Test
    void testReadObjectDefaultProducersFallback() {
        var decoder = new TextDecoder();

        // Typing
        assertInstanceOf(DefaultObject.class, decoder.read("{}"));
        assertInstanceOf(DefaultTypedObject.class, decoder.read("T{}"));

        // Keys
        assertEquals(Map.of("k1", "v1"), decoder.read("{k1: v1}"));
        assertEquals(Map.of("k1", "v1", "k2", "v2"), decoder.read(" { k1 : v1 , k2 : v2 } "));
        assertEquals(Map.of("k1", "v1"), decoder.read("{\"k1\": \"v1\"}"));
        assertEquals(Map.of("null", "v"), decoder.read("{null:v}"));
    }

    @Test
    void testReadObjectDefaultProducersByTypeHint() {
        var decoder = new TextDecoder();

        // Typing
        assertInstanceOf(Hashtable.class, decoder.read("{}", Hashtable.class));
    }

    @Test
    void testReadObjectCustomProducerByName() {
        var repository = new StandardProducerRepository();
        var decoder = new TextDecoder(repository);
        repository.register("M", new ClassObjectProducer(Message.class));

        var result = decoder.read("M{content:test}");

        assertInstanceOf(Message.class, result);
        assertEquals("test", ((Message)result).content);
    }

    @Test
    void testReadObjectCustomProducerByHint() {
        var repository = new StandardProducerRepository();
        var decoder = new TextDecoder(repository);
        repository.register(Message.class, new ClassObjectProducer(Message.class));

        var result = decoder.read("{content:test}", Message.class);

        assertEquals("test", result.content);
    }

    @Test
    void testReadArrayDefaultProducers() {
        var decoder = new TextDecoder();

        // Typing
        assertInstanceOf(DefaultList.class, decoder.read("[]"));
        assertInstanceOf(DefaultTypedList.class, decoder.read("T[]"));

        // Keys
        assertEquals(List.of("e0"), decoder.read("[e0]"));
        assertEquals(List.of("e0", "e1"), decoder.read("[e0, e1]"));
        assertEquals(List.of("e0", "e1", "e2"), decoder.read("[e0, e1, e2]"));
    }

    @Test
    void testReadArrayCustomProducerByName() {
        var repository = new StandardProducerRepository();
        var decoder = new TextDecoder(repository);
        repository.register("SL", new ClassListProducer(StringList.class));

        var result = decoder.read("SL[a, b, c]");

        assertInstanceOf(StringList.class, result);
        assertEquals(List.of("a", "b", "c"), result);
    }

    @Test
    void testReadArrayCustomProducerByHint() {
        var repository = new StandardProducerRepository();
        var decoder = new TextDecoder(repository);
        repository.register(StringList.class, new ClassListProducer(StringList.class));

        var result = decoder.read("[a, b, c]", StringList.class);

        assertEquals(List.of("a", "b", "c"), result);
    }

    @Test
    void testUseCleanDefaultTypes() {
        var decoder = new TextDecoder();

        decoder.setUseCleanDefaultTypes(true);

        assertTrue(decoder.getUseCleanDefaultTypes());
        assertNotInstanceOf(DefaultTypedObject.class, decoder.read("T{}"));
        assertNotInstanceOf(DefaultTypedList.class, decoder.read("T[]"));
        assertNotInstanceOf(DefaultTypedValue.class, decoder.read("T()"));
    }

    @Test
    void testStringEscapedChars() {
        var decoder = new TextDecoder();
        var entries = Map.of(
                "\"\\r\"", "\r",
                "\"\\n\"", "\n",
                "\"\\t\"", "\t",
                "\"\\s\"", " ",
                "\"\\u0000\"", "\0",
                "\"\\\\\"", "\\",
                "\"\\'\"", "'",
                "\"\\\"\"", "\""
        );

        for (var entry : entries.entrySet()) {
            var text = entry.getKey();
            var expected = entry.getValue();
            var result = decoder.read(new SequenceInput(text));

            assertEquals(expected, result, "Expected " + PP.str(text) + " to produce " + PP.str(expected) + ".");
        }
    }

    @Test
    void testInvalidStringEscapedChars() {
        var decoder = new TextDecoder();
        var text = "\"\\x\"";
        var e = assertException(InvalidSyntaxException.class, () -> decoder.read(new SequenceInput(text)));

        assertTrue(e.getMessage().contains("x"));
    }

    @Test
    void testInvalidSyntax() {
        var decoder = new TextDecoder();
        var cases = List.of(
                "???",
                "T<1>",
                "<>(value)"
        );

        for (var text : cases) {
            assertException(
                    InvalidSyntaxException.class,
                    () -> decoder.read(text));
        }
    }

}
