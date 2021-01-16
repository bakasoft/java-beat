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
import static util.TestUtils.assertSameOutput;

class Sample1Test {

    @Test
    void testSample1() throws IOException {
        var stone = new Stone();
        stone.registerObject(Button.class);
        stone.registerObject(Label.class);
        stone.registerObject(Panel.class);
        stone.registerObject(Text.class);

        var text = TestUtils.loadString("/sample1.st");
        var result = stone.readText(text);

        assertTrue(result instanceof Widget, "Result must implement the interface");

        assertSameOutput(stone, result, text);
    }

}
