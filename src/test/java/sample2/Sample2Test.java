package sample2;

import org.junit.jupiter.api.Test;
import org.stonedata.Stone;
import org.stonedata.examiners.Examiners;
import org.stonedata.examiners.ValueExaminer;
import org.stonedata.producers.ValueProducer;
import sample2.types.Document;
import sample2.types.Point;
import sample2.types.Rectangle;
import util.TestUtils;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.SampleUtils.assertLosslessIO;
import static util.TestUtils.assertSameOutput;

class Sample2Test {

    @Test
    void testSample2() throws IOException {
        var stone = Stone.builder()
                .skipEncodingNullFields(true)
                .withObject(Document.class)
                .withObject(Rectangle.class)
                .withProducer(ValueProducer.of((x, y) -> {
                    var point = new Point();
                    point.setX(x.toString());
                    point.setY(y.toString());
                    return point;
                }), Point.class)
                .withExaminer(Examiners.value(Point.class, (p -> List.of(p.getX(), p.getY()))), Point.class)
                .build();

        var text = TestUtils.loadString("/sample2.st");
        var result = stone.readText(text);

        assertTrue(result instanceof Document, "Result must implement the interface");

        assertSameOutput(stone, result, text);

        assertLosslessIO(text);
    }

}
