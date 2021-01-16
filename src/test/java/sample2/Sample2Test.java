package sample2;

import org.junit.jupiter.api.Test;
import org.stonedata.Stone;
import sample2.types.Document;
import sample2.types.Point;
import sample2.types.Rectangle;
import util.TestUtils;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.TestUtils.assertSameOutput;

class Sample2Test {

    @Test
    void testSample2() throws IOException {
        var stone = new Stone();
        stone.setSkipEncodingNullFields(true);
        stone.registerObject(Document.class);
        stone.registerObject(Rectangle.class);
        stone.registerValueProducer(Point.class, (x, y) -> {
            var point = new Point();
            point.setX(x.toString());
            point.setY(y.toString());
            return point;
        });
        stone.registerValueExaminer(Point.class, (p -> List.of(p.getX(), p.getY())));

        var text = TestUtils.loadString("/sample2.st");
        var result = stone.readText(text);

        assertTrue(result instanceof Document, "Result must implement the interface");

        assertSameOutput(stone, result, text);
    }

}
