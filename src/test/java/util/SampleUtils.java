package util;

import org.beat.formats.text.TextDecoder;
import org.beat.formats.text.TextEncoder;
import org.beat.io.standard.AppendableOutput;
import org.beat.io.standard.SequenceInput;
import org.beat.references.impl.StandardReferenceProvider;
import org.beat.references.impl.StandardReferenceTracker;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SampleUtils {

    private static Object decode(String text) {
        var decoder = new TextDecoder(new StandardReferenceTracker());

        return decoder.read(new SequenceInput(text));
    }

    private static String encode(Object value) {
        var encoder = new TextEncoder(new StandardReferenceProvider());
        var outputBuffer = new StringBuilder();

        encoder.write(value, new AppendableOutput(outputBuffer));

        return outputBuffer.toString();
    }

    public static void assertLosslessIO(String text) {
        var obj0 = decode(text);
        var out0 = encode(obj0);
        var obj1 = decode(out0);
        var out1 = encode(obj1);

        assertEquals(obj0, obj1);

        CustomAssertions.assertSameText(out0, out1);
    }

}
