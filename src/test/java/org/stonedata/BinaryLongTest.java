package org.stonedata;

import org.junit.jupiter.api.Test;
import org.stonedata.binary.translate.INT64;
import util.TestUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BinaryLongTest {

    @Test
    public void testPositive1B() {
        testBytes(0,   "00000000");
        testBytes(1,   "00000001");
        testBytes(3,   "00000011");
        testBytes(7,   "00000111");
        testBytes(15,  "00001111");
        testBytes(31,  "00011111");
        testBytes(63,  "00111111");
        testBytes(127, "01111111");
    }

    @Test
    public void testPositive2B() {
        testBytes(pos(8),   "10000000 11111111");
        testBytes(pos(9),   "10000001 11111111");
        testBytes(pos(10),  "10000011 11111111");
        testBytes(pos(11),  "10000111 11111111");
        testBytes(pos(12),  "10001111 11111111");
        testBytes(pos(13),  "10011111 11111111");
        testBytes(pos(14), "10111111 11111111");
    }

    @Test
    public void testPositive3B() {
        testBytes(pos(15), "11000000 01111111 11111111");
        testBytes(pos(16), "11000000 11111111 11111111");
        testBytes(pos(17), "11000001 11111111 11111111");
        testBytes(pos(18), "11000011 11111111 11111111");
        testBytes(pos(19), "11000111 11111111 11111111");
        testBytes(pos(20), "11001111 11111111 11111111");
        testBytes(pos(21), "11011111 11111111 11111111");
    }

    @Test
    public void testPositive4B() {
        testBytes(pos(22), "11100000 00111111 11111111 11111111");
        testBytes(pos(23), "11100000 01111111 11111111 11111111");
        testBytes(pos(24), "11100000 11111111 11111111 11111111");
        testBytes(pos(25), "11100001 11111111 11111111 11111111");
        testBytes(pos(26), "11100011 11111111 11111111 11111111");
        testBytes(pos(27), "11100111 11111111 11111111 11111111");
        testBytes(pos(28), "11101111 11111111 11111111 11111111");
    }

    @Test
    public void testPositive5B() {
        testBytes(pos(29), "11110000 00011111 11111111 11111111 11111111");
        testBytes(pos(30), "11110000 00111111 11111111 11111111 11111111");
        testBytes(pos(31), "11110000 01111111 11111111 11111111 11111111");
        testBytes(pos(32), "11110000 11111111 11111111 11111111 11111111");
        testBytes(pos(33), "11110001 11111111 11111111 11111111 11111111");
        testBytes(pos(34), "11110011 11111111 11111111 11111111 11111111");
        testBytes(pos(35), "11110111 11111111 11111111 11111111 11111111");
    }

    @Test
    public void testPositive6B() {
        testBytes(pos(36), "11111000 00001111 11111111 11111111 11111111 11111111");
        testBytes(pos(37), "11111000 00011111 11111111 11111111 11111111 11111111");
        testBytes(pos(38), "11111000 00111111 11111111 11111111 11111111 11111111");
        testBytes(pos(39), "11111000 01111111 11111111 11111111 11111111 11111111");
        testBytes(pos(40), "11111000 11111111 11111111 11111111 11111111 11111111");
        testBytes(pos(41), "11111001 11111111 11111111 11111111 11111111 11111111");
        testBytes(pos(42), "11111011 11111111 11111111 11111111 11111111 11111111");
    }

    @Test
    public void testPositive7B() {
        testBytes(pos(43), "11111100 00000111 11111111 11111111 11111111 11111111 11111111");
        testBytes(pos(44), "11111100 00001111 11111111 11111111 11111111 11111111 11111111");
        testBytes(pos(45), "11111100 00011111 11111111 11111111 11111111 11111111 11111111");
        testBytes(pos(46), "11111100 00111111 11111111 11111111 11111111 11111111 11111111");
        testBytes(pos(47), "11111100 01111111 11111111 11111111 11111111 11111111 11111111");
        testBytes(pos(48), "11111100 11111111 11111111 11111111 11111111 11111111 11111111");
        testBytes(pos(49), "11111101 11111111 11111111 11111111 11111111 11111111 11111111");
    }

    @Test
    public void testPositive8B() {
        testBytes(pos(50), "11111110 00000011 11111111 11111111 11111111 11111111 11111111 11111111");
        testBytes(pos(51), "11111110 00000111 11111111 11111111 11111111 11111111 11111111 11111111");
        testBytes(pos(52), "11111110 00001111 11111111 11111111 11111111 11111111 11111111 11111111");
        testBytes(pos(53), "11111110 00011111 11111111 11111111 11111111 11111111 11111111 11111111");
        testBytes(pos(54), "11111110 00111111 11111111 11111111 11111111 11111111 11111111 11111111");
        testBytes(pos(55), "11111110 01111111 11111111 11111111 11111111 11111111 11111111 11111111");
        testBytes(pos(56), "11111110 11111111 11111111 11111111 11111111 11111111 11111111 11111111");
    }

    @Test
    public void testPositive9B() {
        testBytes(pos(57), "11111111 00000001 11111111 11111111 11111111 11111111 11111111 11111111 11111111");
        testBytes(pos(58), "11111111 00000011 11111111 11111111 11111111 11111111 11111111 11111111 11111111");
        testBytes(pos(59), "11111111 00000111 11111111 11111111 11111111 11111111 11111111 11111111 11111111");
        testBytes(pos(60), "11111111 00001111 11111111 11111111 11111111 11111111 11111111 11111111 11111111");
        testBytes(pos(61), "11111111 00011111 11111111 11111111 11111111 11111111 11111111 11111111 11111111");
        testBytes(pos(62), "11111111 00111111 11111111 11111111 11111111 11111111 11111111 11111111 11111111");
        testBytes(pos(63), "11111111 01111111 11111111 11111111 11111111 11111111 11111111 11111111 11111111");
    }

    @Test
    public void testNegative1() {
        testBytes(neg(0), "11111111 11111111 11111111 11111111 11111111 11111111 11111111 11111111 11111111");
        testBytes(neg(1), "11111111 11111111 11111111 11111111 11111111 11111111 11111111 11111111 11111110");
        testBytes(neg(2), "11111111 11111111 11111111 11111111 11111111 11111111 11111111 11111111 11111100");
        testBytes(neg(3), "11111111 11111111 11111111 11111111 11111111 11111111 11111111 11111111 11111000");
        testBytes(neg(4), "11111111 11111111 11111111 11111111 11111111 11111111 11111111 11111111 11110000");
        testBytes(neg(5), "11111111 11111111 11111111 11111111 11111111 11111111 11111111 11111111 11100000");
        testBytes(neg(6), "11111111 11111111 11111111 11111111 11111111 11111111 11111111 11111111 11000000");
        testBytes(neg(7), "11111111 11111111 11111111 11111111 11111111 11111111 11111111 11111111 10000000");
        testBytes(neg(8), "11111111 11111111 11111111 11111111 11111111 11111111 11111111 11111111 00000000");
    }

    @Test
    public void testNegative2() {
        testBytes(neg(9),  "11111111 11111111 11111111 11111111 11111111 11111111 11111111 11111110 00000000");
        testBytes(neg(10), "11111111 11111111 11111111 11111111 11111111 11111111 11111111 11111100 00000000");
        testBytes(neg(11), "11111111 11111111 11111111 11111111 11111111 11111111 11111111 11111000 00000000");
        testBytes(neg(12), "11111111 11111111 11111111 11111111 11111111 11111111 11111111 11110000 00000000");
        testBytes(neg(13), "11111111 11111111 11111111 11111111 11111111 11111111 11111111 11100000 00000000");
        testBytes(neg(14), "11111111 11111111 11111111 11111111 11111111 11111111 11111111 11000000 00000000");
        testBytes(neg(15), "11111111 11111111 11111111 11111111 11111111 11111111 11111111 10000000 00000000");
        testBytes(neg(16), "11111111 11111111 11111111 11111111 11111111 11111111 11111111 00000000 00000000");
    }

    @Test
    public void testNegative3() {
        testBytes(neg(17), "11111111 11111111 11111111 11111111 11111111 11111111 11111110 00000000 00000000");
        testBytes(neg(18), "11111111 11111111 11111111 11111111 11111111 11111111 11111100 00000000 00000000");
        testBytes(neg(19), "11111111 11111111 11111111 11111111 11111111 11111111 11111000 00000000 00000000");
        testBytes(neg(20), "11111111 11111111 11111111 11111111 11111111 11111111 11110000 00000000 00000000");
        testBytes(neg(21), "11111111 11111111 11111111 11111111 11111111 11111111 11100000 00000000 00000000");
        testBytes(neg(22), "11111111 11111111 11111111 11111111 11111111 11111111 11000000 00000000 00000000");
        testBytes(neg(23), "11111111 11111111 11111111 11111111 11111111 11111111 10000000 00000000 00000000");
        testBytes(neg(24), "11111111 11111111 11111111 11111111 11111111 11111111 00000000 00000000 00000000");
    }

    @Test
    public void testNegative4() {
        testBytes(neg(25), "11111111 11111111 11111111 11111111 11111111 11111110 00000000 00000000 00000000");
        testBytes(neg(26), "11111111 11111111 11111111 11111111 11111111 11111100 00000000 00000000 00000000");
        testBytes(neg(27), "11111111 11111111 11111111 11111111 11111111 11111000 00000000 00000000 00000000");
        testBytes(neg(28), "11111111 11111111 11111111 11111111 11111111 11110000 00000000 00000000 00000000");
        testBytes(neg(29), "11111111 11111111 11111111 11111111 11111111 11100000 00000000 00000000 00000000");
        testBytes(neg(30), "11111111 11111111 11111111 11111111 11111111 11000000 00000000 00000000 00000000");
        testBytes(neg(31), "11111111 11111111 11111111 11111111 11111111 10000000 00000000 00000000 00000000");
        testBytes(neg(32), "11111111 11111111 11111111 11111111 11111111 00000000 00000000 00000000 00000000");
    }

    @Test
    public void testNegative5() {
        testBytes(neg(32), "11111111 11111111 11111111 11111111 11111111 00000000 00000000 00000000 00000000");
        testBytes(neg(33), "11111111 11111111 11111111 11111111 11111110 00000000 00000000 00000000 00000000");
        testBytes(neg(34), "11111111 11111111 11111111 11111111 11111100 00000000 00000000 00000000 00000000");
        testBytes(neg(35), "11111111 11111111 11111111 11111111 11111000 00000000 00000000 00000000 00000000");
        testBytes(neg(36), "11111111 11111111 11111111 11111111 11110000 00000000 00000000 00000000 00000000");
        testBytes(neg(37), "11111111 11111111 11111111 11111111 11100000 00000000 00000000 00000000 00000000");
        testBytes(neg(38), "11111111 11111111 11111111 11111111 11000000 00000000 00000000 00000000 00000000");
        testBytes(neg(39), "11111111 11111111 11111111 11111111 10000000 00000000 00000000 00000000 00000000");
        testBytes(neg(40), "11111111 11111111 11111111 11111111 00000000 00000000 00000000 00000000 00000000");
    }

    @Test
    public void testNegative6() {
        testBytes(neg(41), "11111111 11111111 11111111 11111110 00000000 00000000 00000000 00000000 00000000");
        testBytes(neg(42), "11111111 11111111 11111111 11111100 00000000 00000000 00000000 00000000 00000000");
        testBytes(neg(43), "11111111 11111111 11111111 11111000 00000000 00000000 00000000 00000000 00000000");
        testBytes(neg(44), "11111111 11111111 11111111 11110000 00000000 00000000 00000000 00000000 00000000");
        testBytes(neg(45), "11111111 11111111 11111111 11100000 00000000 00000000 00000000 00000000 00000000");
        testBytes(neg(46), "11111111 11111111 11111111 11000000 00000000 00000000 00000000 00000000 00000000");
        testBytes(neg(47), "11111111 11111111 11111111 10000000 00000000 00000000 00000000 00000000 00000000");
        testBytes(neg(48), "11111111 11111111 11111111 00000000 00000000 00000000 00000000 00000000 00000000");
    }

    @Test
    public void testNegative7() {
        testBytes(neg(49), "11111111 11111111 11111110 00000000 00000000 00000000 00000000 00000000 00000000");
        testBytes(neg(50), "11111111 11111111 11111100 00000000 00000000 00000000 00000000 00000000 00000000");
        testBytes(neg(51), "11111111 11111111 11111000 00000000 00000000 00000000 00000000 00000000 00000000");
        testBytes(neg(52), "11111111 11111111 11110000 00000000 00000000 00000000 00000000 00000000 00000000");
        testBytes(neg(53), "11111111 11111111 11100000 00000000 00000000 00000000 00000000 00000000 00000000");
        testBytes(neg(54), "11111111 11111111 11000000 00000000 00000000 00000000 00000000 00000000 00000000");
        testBytes(neg(55), "11111111 11111111 10000000 00000000 00000000 00000000 00000000 00000000 00000000");
        testBytes(neg(56), "11111111 11111111 00000000 00000000 00000000 00000000 00000000 00000000 00000000");
    }

    @Test
    public void testNegative8() {
        testBytes(neg(57), "11111111 11111110 00000000 00000000 00000000 00000000 00000000 00000000 00000000");
        testBytes(neg(58), "11111111 11111100 00000000 00000000 00000000 00000000 00000000 00000000 00000000");
        testBytes(neg(59), "11111111 11111000 00000000 00000000 00000000 00000000 00000000 00000000 00000000");
        testBytes(neg(60), "11111111 11110000 00000000 00000000 00000000 00000000 00000000 00000000 00000000");
        testBytes(neg(61), "11111111 11100000 00000000 00000000 00000000 00000000 00000000 00000000 00000000");
        testBytes(neg(62), "11111111 11000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000");
        // TODO clarify following cases:
        // testBytes(neg(63), "11111111 10000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000");
        // testBytes(neg(64), "11111111 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000");
    }

    private static long pos(int n) {
        return Long.parseLong("1".repeat(n), 2);
    }

    private static long neg(int n) {
        return -(long)Math.pow(2, n);
    }

    private static void testBytes(long number, String expectedBinary) {
        var actual = INT64.bytesOfLong(number);
        var expected = TestUtils.parseBinaryBytes(expectedBinary);
        var actualBin = TestUtils.formatBinaryBytes(actual);
        var expectedBin = TestUtils.formatBinaryBytes(expected);

        assertEquals(expectedBin, actualBin);

        try {
            var result = INT64.readRawLong(new ByteArrayInputStream(actual));

            assertEquals(number, result, "Resulting number");
        }
        catch (IOException e) {
            throw new AssertionError(e);
        }
    }

}
