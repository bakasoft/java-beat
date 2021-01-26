package sample2;

import org.junit.jupiter.api.Test;
import org.beat.Beat;
import org.beat.examiners.Examiners;
import org.beat.producers.ValueProducer;
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
        var beat = Beat.builder()
                .skipNullFields(true)
                .withObject(Document.class)
                .withObject(Rectangle.class)
                .withProducer(Point.class, ValueProducer.of((x, y) -> {
                        var point = new Point();
                        point.setX(x.toString());
                        point.setY(y.toString());
                        return point;
                }))
                .withExaminer(Examiners.value(Point.class, (p ->
                        List.of(p.getX(), p.getY()))), Point.class)
                .build();

        var text = TestUtils.loadString("/sample2.st");
        var result = beat.readText(text);

        assertTrue(result instanceof Document, "Result must implement the interface");

        assertSameOutput(beat, result, text);

        assertLosslessIO(text);
    }

}
