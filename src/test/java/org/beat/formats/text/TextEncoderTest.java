package org.beat.formats.text;

import org.junit.jupiter.api.Test;
import org.beat.errors.CyclicDocumentException;
import org.beat.errors.UnsupportedValueException;
import org.beat.examiners.Examiner;
import org.beat.examiners.Examiners;
import org.beat.examiners.standard.array.ArrayInstanceExaminer;
import org.beat.examiners.standard.array.ListExaminer;
import org.beat.examiners.standard.object.ClassObjectExaminer;
import org.beat.examiners.standard.object.MapExaminer;
import org.beat.examiners.standard.value.DefaultTypedValueExaminer;
import org.beat.examiners.standard.value.ValueIdentityExaminer;
import org.beat.references.ReferenceProvider;
import org.beat.references.impl.StandardReferenceProvider;
import org.beat.repositories.standard.StandardExaminerRepository;
import org.beat.types.standard.DefaultTypedListImpl;
import org.beat.types.standard.DefaultTypedObjectImpl;
import org.beat.types.standard.DefaultTypedValueImpl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static util.CustomAssertions.assertContains;
import static util.CustomAssertions.assertException;

class TextEncoderTest {

    public static class CyclicNode { public CyclicNode inner; }

    @Test
    void testSearchCustomExaminer() {
        var repository = new StandardExaminerRepository()
                .register(Examiners.value(Object::toString), Class.class);
        var encoder = new TextEncoder(repository);
        var value = List.class;
        var text = encoder.write(value);

        assertEquals("\"interface java.util.List\"", text);
    }

    @Test
    void testSearchDefaultAtomicExaminers() {
        var encoder = new TextEncoder();

        // null
        assertEquals("null", encoder.write(null));

        // String / Character
        assertEquals("\"!@#\"", encoder.write("!@#"));
        assertEquals("\"?\"", encoder.write('?'));

        // Boolean
        assertEquals("true", encoder.write(true));
        assertEquals("false", encoder.write(false));

        // Number
        assertEquals("1", encoder.write(1));
        assertEquals("1.5", encoder.write(1.5));
    }

    @Test
    void testSearchDefaultTypedExaminers() {
        var encoder = new TextEncoder();

        // DefaultTypedObject
        assertEquals("T{}", encoder.write(new DefaultTypedObjectImpl("T")));
        assertEquals("{}", encoder.write(new DefaultTypedObjectImpl()));

        // DefaultTypedList
        assertEquals("T[]", encoder.write(new DefaultTypedListImpl("T")));
        assertEquals("[]", encoder.write(new DefaultTypedListImpl()));

        // DefaultTypedValue
        assertEquals("T()", encoder.write(new DefaultTypedValueImpl("T")));
        assertEquals("()", encoder.write(new DefaultTypedValueImpl()));
    }

    @Test
    void testSearchDefaultUntypedExaminers() {
        var encoder = new TextEncoder();

        // Map
        assertEquals("{}", encoder.write(Map.of()));

        // List
        assertEquals("[]", encoder.write(List.of()));
    }

    @Test
    void testSearchDefaultExaminersByClass() {
        var encoder = new TextEncoder();

        // Array
        assertEquals("[1,2,3]", encoder.write(new int[]{1,2,3}));

        // Enum
        assertEquals("HOURS", encoder.write(TimeUnit.HOURS));

        // Other
        assertEquals("{}", encoder.write(new Object()));
    }

    @Test
    void testWriteContent() {
        var encoder = new TextEncoder();

        assertEquals("{}", encoder.write(new Object()));
        assertEquals("[]", encoder.write(new Object[0]));
        assertEquals("0", encoder.write(0));
    }

    @Test
    void testUnsupportedExaminer() {
        Examiner invalidExaminer = (() -> null);

        var repository = new StandardExaminerRepository()
                .register(invalidExaminer, Object.class);
        var encoder = new TextEncoder(repository);
        var e = assertException(
                UnsupportedValueException.class,
                () -> encoder.write(new Object()));

        assertContains(invalidExaminer.getClass().getName(), e.getMessage());
    }

    @Test
    void testWriteTypeAndContent() {
        var repository = new StandardExaminerRepository();
        var encoder = new TextEncoder(repository);

        repository.register(new MapExaminer("T"), Map.class);
        repository.register(new ListExaminer("U"), List.class);
        repository.register(new DefaultTypedValueExaminer("V"), Integer.class);

        assertEquals("T{}", encoder.write(Map.of()));
        assertEquals("U[]", encoder.write(List.of()));
        assertEquals("V(0)", encoder.write(0));
    }

    @Test
    void testWriteTypeReferenceAndContent() {
        var repository = new StandardExaminerRepository();
        var references = new StandardReferenceProvider();
        var encoder = new TextEncoder(repository, references);

        repository.register(new MapExaminer("T"), Map.class);
        repository.register(new ArrayInstanceExaminer("U"), Object[].class);
        repository.register(new DefaultTypedValueExaminer("V"), Integer.class);

        var someObject = Map.of();
        var someArray = new Object[0];
        var someValue = 0;

        references.setReference(someObject, "1");
        references.setReference(someArray, "2");
        references.setReference(someValue, "3");

        assertEquals("[T<1>{},<1>]", encoder.write(List.of(someObject, someObject)));
        assertEquals("[U<2>[],<2>]", encoder.write(List.of(someArray, someArray)));
        assertEquals("[V<3>(0),<3>]", encoder.write(List.of(someValue, someValue)));
    }

    @Test
    void testWriteReferenceAndContent() {
        var references = new StandardReferenceProvider();
        var encoder = new TextEncoder(references);

        var someObject = new Object();
        var someArray = new Object[0];
        var someValue = 0;

        references.setReference(someObject, "1");
        references.setReference(someArray, "2");
        references.setReference(someValue, "3");

        assertEquals("[<1>{},<1>]", encoder.write(List.of(someObject, someObject)));
        assertEquals("[<2>[],<2>]", encoder.write(List.of(someArray, someArray)));
        assertEquals("[<3>(0),<3>]", encoder.write(List.of(someValue, someValue)));
    }

    @Test
    void testCyclicException() {
        var node = new CyclicNode();
        node.inner = node;

        var repository = new StandardExaminerRepository();
        repository.register(
                new ClassObjectExaminer(CyclicNode.class),
                CyclicNode.class
        );

        var encoder = new TextEncoder(repository);
        var e = assertException(
                CyclicDocumentException.class,
                () -> encoder.write(node));

        assertContains(CyclicNode.class.getName(), e.getMessage());
    }

    @Test
    void testWriteObject() {
        var encoder = new TextEncoder();

        var mapEmpty = Map.of();
        var mapSingleKey = Map.of("a", 1);

        var mapSortedKeys = new LinkedHashMap<>();
        mapSortedKeys.put("a", 1);
        mapSortedKeys.put("b", 2);

        var mapWithNull = new HashMap<>();
        mapWithNull.put("x", null);

        assertEquals("{}", encoder.write(mapEmpty));
        assertEquals("{a:1}", encoder.write(mapSingleKey));
        assertEquals("{a:1,b:2}", encoder.write(mapSortedKeys));
        assertEquals("{x:null}", encoder.write(mapWithNull));
    }

    @Test
    void testWriteObjectWithSkipNullFields() {
        var encoder = new TextEncoder();
        encoder.setSkipNullFields(true);

        var mapWithNull = new LinkedHashMap<String, Object>();
        mapWithNull.put("a", 1);
        mapWithNull.put("b", null);
        mapWithNull.put("c", 2);

        assertTrue(encoder.getSkipNullFields());
        assertEquals("{a:1,c:2}", encoder.write(mapWithNull));
    }

    @Test
    void testWriteArray() {
        var encoder = new TextEncoder();

        var listEmpty = List.of();
        var listMultiItems = List.of(1, 2, 3);

        assertEquals("[]", encoder.write(listEmpty));
        assertEquals("[1,2,3]", encoder.write(listMultiItems));
    }

    @Test
    void testUnsupportedValue() {
        var repository = new StandardExaminerRepository();
        var encoder = new TextEncoder(repository);
        repository.register(ValueIdentityExaminer.INSTANCE, Object.class);

        var e = assertException(
                UnsupportedValueException.class,
                () -> encoder.write(new Object()));

        assertContains(Object.class.getName(), e.getMessage());
    }

    @Test
    void testWriteAtomicValues() {
        var encoder = new TextEncoder();

        assertEquals("null", encoder.write(null));
        assertEquals("true", encoder.write(true));
        assertEquals("false", encoder.write(false));
        assertEquals("abc", encoder.write("abc"));
        assertEquals("\"x\"", encoder.write('x'));
    }

    @Test
    void testWriteAtomicValuesWrapped() {
        var encoder = new TextEncoder((ReferenceProvider)(value -> "1"));

        assertEquals("<1>(null)", encoder.write(null));
        assertEquals("<1>(true)", encoder.write(true));
        assertEquals("<1>(false)", encoder.write(false));
        assertEquals("<1>(abc)", encoder.write("abc"));
        assertEquals("<1>(\"x\")", encoder.write('x'));
    }

    @Test
    void testWriteArguments() {
        var repository = new StandardExaminerRepository();
        var encoder = new TextEncoder(repository);
        repository.register(ValueIdentityExaminer.INSTANCE, List.class);

        assertEquals("()", encoder.write(List.of()));
        assertEquals("1", encoder.write(List.of(1)));
        assertEquals("(1,2)", encoder.write(List.of(1,2)));
        assertEquals("(1,2,3)", encoder.write(List.of(1,2,3)));
    }

    @Test
    void testEscapedChars() {
        var encoder = new TextEncoder();
        var text = encoder.write("\t\r\n\"");

        assertEquals("\"\\t\\r\\n\\\"\"", text);
    }

}
