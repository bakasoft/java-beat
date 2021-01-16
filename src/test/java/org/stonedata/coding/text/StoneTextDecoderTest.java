package org.stonedata.coding.text;

import org.junit.jupiter.api.Test;
import org.stonedata.Stone;
import org.stonedata.errors.InvalidSyntaxException;
import org.stonedata.errors.UnknownReferenceException;
import org.stonedata.types.array.SoftTypedList;
import org.stonedata.types.object.SoftTypedObject;
import org.stonedata.types.value.EmptyValue;
import org.stonedata.types.object.UntypedObject;
import org.stonedata.types.array.UntypedList;
import org.stonedata.types.value.SoftTypedValue;
import org.stonedata.util.PP;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.CustomAssertions.assertException;
import static util.CustomAssertions.assertInstanceOf;

class StoneTextDecoderTest {

    public static class HardTypedObject {}

    public static class HardTypedList extends ArrayList<Object> {}

    @Test
    void testEmptyObject() throws IOException {
        var stone = new Stone();
        var result = stone.readText("{}");

        assertInstanceOf(UntypedObject.class, result);
    }

    @Test
    void testSoftTypedObject() throws IOException {
        var stone = new Stone();
        var result = (SoftTypedObject)stone.readText("SomeType {}");

        assertEquals("SomeType", result.getTypeName());
    }

    @Test
    void testHardTypedObject() throws IOException {
        var stone = new Stone();

        stone.registerObjectProducer(HardTypedObject.class);

        var result = stone.readText("HardTypedObject {}");

        assertInstanceOf(HardTypedObject.class, result);
    }

    @Test
    void testEmptyArray() throws IOException {
        var stone = new Stone();
        var result = stone.readText("[]");

        assertInstanceOf(UntypedList.class, result);
    }

    @Test
    void testSoftTypedArray() throws IOException {
        var stone = new Stone();
        var result = (SoftTypedList)stone.readText("SomeType []");

        assertEquals("SomeType", result.getTypeName());
    }

    @Test
    void testHardTypedArray() throws IOException {
        var stone = new Stone();

        stone.registerArrayProducer(HardTypedList.class);

        var result = stone.readText("HardTypedList []");

        assertInstanceOf(HardTypedList.class, result);
    }

    @Test
    void testEmptyValue() throws IOException {
        var stone = new Stone();
        var result = stone.readText("()");

        assertInstanceOf(EmptyValue.class, result);
    }

    @Test
    void testNullValue() throws IOException {
        var stone = new Stone();
        var result = stone.readText("null");

        assertNull(result);
    }

    @Test
    void testTrueValue() throws IOException {
        var stone = new Stone();
        var result = stone.readText("true");

        assertEquals(true, result);
    }

    @Test
    void testFalseValue() throws IOException {
        var stone = new Stone();
        var result = stone.readText("false");

        assertEquals(false, result);
    }

    @Test
    void testIntegerValue() throws IOException {
        var stone = new Stone();
        var result = stone.readText("12345");

        assertEquals(new BigInteger("12345"), result);
    }

    @Test
    void testDecimalValue() throws IOException {
        var stone = new Stone();
        var result = stone.readText("12.34");

        assertEquals(new BigDecimal("12.34"), result);
    }

    @Test
    void testSoftTypedValue() throws IOException {
        var stone = new Stone();
        var result = (SoftTypedValue)stone.readText("SomeValue()");

        assertEquals("SomeType", result.getTypeName());
        assertEquals(0, result.getArguments().length);
    }

    @Test
    void testObjectWithStringKeys() throws IOException {
        var stone = new Stone();
        var result = stone.readText("{\"key\": value}", Map.class);

        assertEquals("value", result.get("key"));
    }

    @Test
    void testUnknownReferenceNoType() {
        var stone = new Stone();
        var text = "{value: <SOME_REF>}";
        var e = assertException(
                UnknownReferenceException.class,
                () -> stone.readText(text));

        assertTrue(e.getMessage().contains("SOME_REF"));
    }

    @Test
    void testUnknownReferenceWithType() {
        var stone = new Stone();
        var text = "{value: SOME_TYPE<SOME_REF>}";
        var e = assertException(
                UnknownReferenceException.class,
                () -> stone.readText(text));

        assertTrue(e.getMessage().contains("SOME_TYPE"));
        assertTrue(e.getMessage().contains("SOME_REF"));
    }

    @Test
    void testObjectReference() throws IOException {
        var stone = new Stone();
        var text = "{original: <1> {}, reference: <1>}";
        var result = stone.readText(text, Map.class);

        assertInstanceOf(Map.class, result.get("original"));
        assertSame(result.get("original"), result.get("reference"));
    }

    @Test
    void testArrayReference() throws IOException {
        var stone = new Stone();
        var text = "{original: <1> [], reference: <1>}";
        var result = stone.readText(text, Map.class);

        assertInstanceOf(List.class, result.get("original"));
        assertSame(result.get("original"), result.get("reference"));
    }

    @Test
    void testValueReference() throws IOException {
        var stone = new Stone();
        var text = "{original: <1>(123), reference: <1>}";
        var result = stone.readText(text, Map.class);

        assertInstanceOf(Number.class, result.get("original"));
        assertSame(result.get("original"), result.get("reference"));
    }

    @Test
    void testInvalidReference() {
        var stone = new Stone();
        var text = "<>(value)";

        assertException(
                InvalidSyntaxException.class,
                () -> stone.readText(text, Map.class));
    }

    @Test
    void testStringEscapedChars() throws IOException {
        var stone = new Stone();
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
            var result = stone.readText(text);

            assertEquals(expected, result, "Expected " + PP.str(text) + " to produce " + PP.str(expected) + ".");
        }
    }

    @Test
    void testStringEscapedLF() throws IOException {
        var stone = new Stone();
        var text = "\"\\n\"";
        var result = stone.readText(text);

        assertEquals("\n", result);
    }

    @Test
    void testInvalidStringEscapedChars() {
        var stone = new Stone();
        var text = "\"\\x\"";
        var e = assertException(InvalidSyntaxException.class, () -> stone.readText(text));

        assertTrue(e.getMessage().contains("x"));
    }

    @Test
    void testReadDuration() throws IOException {
        var stone = new Stone();
        var text = "PT23H59M60S";
        var d = stone.readText(text, Duration.class);

        assertEquals(86400, d.get(ChronoUnit.SECONDS));
    }

    @Test
    void testArrayMultipleItems() throws IOException {
        var stone = new Stone();
        var text = "[null, true, false, 1, 0.5, 'abc', [], {}]";
        var result = (UntypedList)stone.readText(text);

        assertNull(result.get(0));
        assertEquals(true, result.get(1));
        assertEquals(false, result.get(2));
        assertEquals(BigInteger.ONE, result.get(3));
        assertEquals(new BigDecimal("0.5"), result.get(4));
        assertEquals("abc", result.get(5));
        assertTrue(result.get(6) instanceof List);
        assertTrue(result.get(7) instanceof Map);
        assertEquals(8, result.size());
    }

}
