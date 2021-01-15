package sample;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.stonedata.Stone;
import org.stonedata.errors.UnknownReferenceException;
import org.stonedata.util.GenericList;
import org.stonedata.util.GenericObject;
import org.stonedata.util.GenericValue;

import java.io.IOException;
import java.lang.reflect.GenericArrayType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static util.TestUtils.assertSameOutput;

class SampleTest {

    @Test
    void testEmptyObject() throws IOException {
        var stone = new Stone();
        var text = "{}";
        var result = stone.readText(text);

        assertTrue(result instanceof GenericObject, "Must be generic result");
        assertSameOutput(stone, result, text);
    }

    @Test
    void testEmptyArray() throws IOException {
        var stone = new Stone();
        var text = "[]";
        var result = stone.readText(text);

        assertTrue(result instanceof GenericList, "Must be generic result");
        assertSameOutput(stone, result, text);
    }

    @Test
    void testEmptyValue() throws IOException {
        var stone = new Stone();
        var text = "()";
        var result = stone.readText(text);

        assertTrue(result instanceof GenericValue, "Must be generic result");
        assertSameOutput(stone, result, text);
    }

    @Test
    void testUnknownReferenceNoType() throws IOException {
        var stone = new Stone();
        var text = "{value: <SOME_REF>}";

        try {
            stone.readText(text);

            fail("Expected exception of " + UnknownReferenceException.class);
        }
        catch (UnknownReferenceException e) {
            assertTrue(e.getMessage().contains("SOME_REF"));
        }
    }

    @Test
    void testUnknownReferenceWithType() throws IOException {
        var stone = new Stone();
        var text = "{value: SOME_TYPE<SOME_REF>}";

        try {
            stone.readText(text);

            fail("Expected exception of " + UnknownReferenceException.class);
        }
        catch (UnknownReferenceException e) {
            assertTrue(e.getMessage().contains("SOME_TYPE"));
            assertTrue(e.getMessage().contains("SOME_REF"));
        }
    }

    @Test
    void testObjectReference() throws IOException {
        var stone = new Stone();
        var text = "{value0: <1> {}, value1: <1>}";
        var result = (GenericObject) stone.readText(text);

        assertSame(result.get("value0"), result.get("value1"));
    }

    @Test
    void testArrayReference() throws IOException {
        var stone = new Stone();
        var text = "{value0: <1> [], value1: <1>}";
        var result = (GenericObject) stone.readText(text);

        assertSame(result.get("value0"), result.get("value1"));
    }

    @Test
    void testStringEscapedChars() throws IOException {
        var stone = new Stone();
        var text = "\"\\r\\n\\t\\s\\u0000\\\"\\'\"";
        var result = (GenericObject) stone.readText(text);

        assertSame(result.get("value0"), result.get("value1"));
    }

}
