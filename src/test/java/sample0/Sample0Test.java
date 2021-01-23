package sample0;

import static util.SampleUtils.assertLosslessIO;
import static util.TestUtils.assertSameOutput;

import org.junit.jupiter.api.Test;
import org.stonedata.Stone;
import org.stonedata.examiners.standard.value.DurationExaminer;
import org.stonedata.producers.standard.value.DurationProducer;
import sample0.types.Release;
import util.TestUtils;

import java.io.IOException;
import java.time.Duration;

class Sample0Test {

    @Test
    void testSample0() throws IOException {
        var stone = Stone.builder()
                .withType(Duration.class, new DurationExaminer(), new DurationProducer())
                .build();
        var text = TestUtils.loadString("/sample0.st");
        var result = stone.readText(text, Release.class);

        assertSameOutput(stone, result, text);

        assertLosslessIO(text);
    }

}
