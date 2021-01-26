package sample0;

import static util.SampleUtils.assertLosslessIO;
import static util.TestUtils.assertSameOutput;

import org.beat.Beat;
import org.junit.jupiter.api.Test;
import org.beat.examiners.standard.value.DurationExaminer;
import org.beat.producers.standard.value.DurationProducer;
import sample0.types.Release;
import util.TestUtils;

import java.time.Duration;

class Sample0Test {

    @Test
    void testSample0() {
        var beat = Beat.builder()
                .withType(Duration.class, DurationExaminer.ANONYMOUS_INSTANCE, DurationProducer.INSTANCE)
                .build();
        var text = TestUtils.loadString("/sample0.st");
        var result = beat.readText(text, Release.class);

        assertSameOutput(beat, result, text);

        assertLosslessIO(text);
    }

}
