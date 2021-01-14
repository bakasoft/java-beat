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

class Sample1Test {

    @Test
    void testSample1() throws IOException {
        var stone = new Stone();
        stone.register(Button.class);
        stone.register(Label.class);
        stone.register(Panel.class);
        stone.register(Text.class);

        var text = TestUtils.loadString("/sample1.st");
        var widget = stone.readText(text);

        assertTrue(widget instanceof Widget, "Result must implement the interface");

        var buffer = new StringBuilder();

        stone.writeText(widget, buffer, true);

        assertEquals(text, buffer.toString());
    }

}
