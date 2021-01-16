package org.stonedata.coding.text;

import org.junit.jupiter.api.Test;
import org.stonedata.errors.CyclicDocumentException;
import org.stonedata.errors.UnsupportedValueException;
import org.stonedata.examiners.Examiner;
import org.stonedata.examiners.ValueExaminer;
import org.stonedata.examiners.standard.array.ListExaminer;
import org.stonedata.examiners.standard.object.ClassObjectExaminer;
import org.stonedata.examiners.standard.object.MapExaminer;
import org.stonedata.examiners.standard.value.DurationExaminer;
import org.stonedata.io.impl.AppendableOutput;
import org.stonedata.io.impl.NullOutput;
import org.stonedata.references.ReferenceProvider;
import org.stonedata.references.impl.NullReferenceProvider;
import org.stonedata.references.impl.StandardReferenceProvider;
import org.stonedata.repositories.ExaminerRepository;
import org.stonedata.repositories.standard.NullExaminerRepository;
import org.stonedata.repositories.standard.StandardExaminerRepository;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.CustomAssertions.assertException;

class StoneTextEncoderTest {

    public static class CyclicNode {
        public CyclicNode inner;
    }

    @Test
    void testWriteNull() {
        var encoder = buildEncoder();
        var text = write(encoder, null);

        assertEquals("null", text);
    }

    @Test
    void testWriteTrue() {
        var encoder = buildEncoder();
        var text = write(encoder, true);

        assertEquals("true", text);
    }

    @Test
    void testWriteFalse() {
        var encoder = buildEncoder();
        var text = write(encoder, true);

        assertEquals("true", text);
    }

    @Test
    void testWriteString() {
        var encoder = buildEncoder();
        var text = write(encoder, "abc");

        assertEquals("abc", text);
    }

    @Test
    void testWriteStringEscaped() {
        var encoder = buildEncoder();
        var text = write(encoder, "\t\r\n\"");

        assertEquals("\"\\t\\r\\n\\\"\"", text);
    }

    @Test
    void testWriteNumber() {
        var encoder = buildEncoder();
        var text = write(encoder, 123);

        assertEquals("123", text);
    }

    @Test
    void testWriteChar() {
        var encoder = buildEncoder();
        var text = write(encoder, 'x');

        assertEquals("\"x\"", text);
    }

    @Test
    void testWriteUnsupportedValue() {
        var encoder = buildEncoder();

        assertException(
                UnsupportedValueException.class,
                () -> write(encoder, new Object()));
    }

    @Test
    void testWriteStandardValueWithReference() {
        var references = new StandardReferenceProvider();
        var someValue = "SomeValue";

        references.setReference(null, someValue, "REF");

        var encoder = buildEncoder(references);
        var text = write(encoder, someValue);

        assertEquals("<REF>(SomeValue)", text);
    }

    @Test
    void testWriteEmptyObject() {
        var map = Map.of();
        var repository = new StandardExaminerRepository();

        repository.registerForValue(new MapExaminer(null), map);

        var encoder = buildEncoder(repository);
        var text = write(encoder, map);

        assertEquals("{}", text);
    }

    @Test
    void testSkipNullFields() {
        var map = new HashMap<String, Object>();
        map.put("a", 1);
        map.put("b", null);
        map.put("c", 2);
        var repository = new StandardExaminerRepository();

        repository.registerForValue(new MapExaminer(null), map);

        var encoder = buildEncoder(repository);
        encoder.setSkipNullFields(true);

        assertTrue(encoder.isSkipNullFields());

        var text = write(encoder, map);

        assertEquals("{a:1,c:2}", text);
    }

    @Test
    void testWriteNamedMap() {
        var map = Map.of("k1", "v1");
        var repository = new StandardExaminerRepository();

        repository.registerForValue(new MapExaminer("Map"), map);

        var encoder = buildEncoder(repository);
        var text = write(encoder, map);

        assertEquals("Map{k1:v1}", text);
    }

    @Test
    void testWriteUnnamedMap() {
        var map = Map.of("k1", "v1");
        var repository = new StandardExaminerRepository();

        repository.registerForValue(new MapExaminer(null), map);

        var encoder = buildEncoder(repository);
        var text = write(encoder, map);

        assertEquals("{k1:v1}", text);
    }

    @Test
    void testWriteNamedMapWithRef() {
        var repository = new StandardExaminerRepository();
        var references = new StandardReferenceProvider();
        var map = Map.of("k1", "v1");

        repository.registerForValue(new MapExaminer("Map"), map);
        references.setReference("Map", map, "1");

        var encoder = new StoneTextEncoder(repository, references);
        var text = write(encoder, map);

        assertEquals("Map<1>{k1:v1}", text);
    }

    @Test
    void testWriteUnnamedMapWithRef() {
        var repository = new StandardExaminerRepository();
        var references = new StandardReferenceProvider();
        var map = Map.of("k1", "v1");

        repository.registerForValue(new MapExaminer(null), map);
        references.setReference(null, map, "1");

        var encoder = new StoneTextEncoder(repository, references);
        var text = write(encoder, map);

        assertEquals("<1>{k1:v1}", text);
    }

    @Test
    void testCyclicException() {
        var repository = new StandardExaminerRepository();
        repository.registerForCondition(
                new ClassObjectExaminer(CyclicNode.class, "Node"),
                value -> value instanceof CyclicNode);

        var encoder = buildEncoder(repository);
        var node = new CyclicNode();

        node.inner = node;

        assertException(
                CyclicDocumentException.class,
                () -> encoder.write(new NullOutput(), node));
    }

    @Test
    void testWriteArray() {
        var repository = new StandardExaminerRepository();
        repository.registerForCondition(
                new ListExaminer("List"),
                value -> value instanceof List);

        var encoder = buildEncoder(repository);

        var text = write(encoder, List.of(1, 2, 3));

        assertEquals("List[1,2,3]", text);
    }

    @Test
    void testWriteEmptyArray() {
        var repository = new StandardExaminerRepository();
        repository.registerForCondition(
                new ListExaminer("List"),
                value -> value instanceof List);

        var encoder = buildEncoder(repository);

        var text = write(encoder, List.of());

        assertEquals("List[]", text);
    }

    @Test
    void testWriteValue() {
        var repository = new StandardExaminerRepository();
        repository.registerForCondition(
                new DurationExaminer("Duration"),
                value -> value instanceof Duration);

        var encoder = buildEncoder(repository);

        var text = write(encoder, Duration.of(1, ChronoUnit.SECONDS));

        assertEquals("Duration(PT1S)", text);
    }

    @Test
    void testWriteMultiValue() {
        class Point {
            public int x, y;
        }
        var pointExaminer = new ValueExaminer() {

            @Override
            public List<Object> computeArguments(Object value) {
                var p = (Point)value;
                return List.of(p.x, p.y);
            }

            @Override
            public String getType() {
                return "Point";
            }
        };
        var repository = new StandardExaminerRepository();
        repository.registerForCondition(
                pointExaminer,
                value -> value instanceof Point);

        var encoder = buildEncoder(repository);
        var point = new Point();
        var text = write(encoder, point);

        assertEquals("Point(0,0)", text);
    }

    @Test
    void testInvalidExaminer() {
        var repository = new StandardExaminerRepository();
        repository.registerForCondition(
                () -> "Dummy",
                value -> true);

        var encoder = buildEncoder(repository);

        assertException(
                UnsupportedValueException.class,
                () -> write(encoder, 1));
    }

    private static StoneTextEncoder buildEncoder() {
        var repository = NullExaminerRepository.INSTANCE;
        var references = NullReferenceProvider.INSTANCE;
        return new StoneTextEncoder(repository, references);
    }

    private static StoneTextEncoder buildEncoder(ExaminerRepository repository) {
        var references = NullReferenceProvider.INSTANCE;
        return new StoneTextEncoder(repository, references);
    }

    private static StoneTextEncoder buildEncoder(ReferenceProvider references) {
        var examiners = NullExaminerRepository.INSTANCE;
        return new StoneTextEncoder(examiners, references);
    }

    private static String write(StoneTextEncoder encoder, Object value) {
        var buffer = new StringBuilder();
        var output = new AppendableOutput(buffer);

        try {
            encoder.write(output, value);
        }
        catch (IOException e) {
            throw new AssertionError(e);
        }

        return buffer.toString();
    }

}
