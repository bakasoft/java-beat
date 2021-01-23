package sample1;

import org.junit.jupiter.api.Test;
import org.stonedata.Stone;
import sample1.types.Button;
import sample1.types.Label;
import sample1.types.Panel;
import sample1.types.Text;
import sample1.types.Widget;
import util.TestUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.SampleUtils.assertLosslessIO;
import static util.TestUtils.assertSameOutput;

class Sample1Test {

    @Test
    void testSample1() throws IOException {
        var stone = Stone.builder()
                .withObject(Button.class)
                .withObject(Label.class)
                .withObject(Panel.class)
                .withObject(Text.class)
                .build();
        var text = TestUtils.loadString("/sample1.st");
        var result = stone.readText(text);

        assertTrue(result instanceof Widget, "Result must implement the interface");

        assertSameOutput(stone, result, text);

        assertLosslessIO(text);
    }

}
