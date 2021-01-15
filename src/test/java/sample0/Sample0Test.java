package sample0;

import static org.junit.jupiter.api.Assertions.*;
import static util.TestUtils.assertSameOutput;

import org.junit.jupiter.api.Test;
import org.stonedata.Stone;
import sample0.types.Release;
import util.TestUtils;

import java.io.IOException;

class Sample0Test {

    @Test
    void testSample0() throws IOException {
        var stone = new Stone();
        var text = TestUtils.loadString("/sample0.st");
        var result = stone.readText(text, Release.class);

        assertSameOutput(stone, result, text);
    }

}
