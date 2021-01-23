package util;

import org.stonedata.formats.text.TextDecoder;
import org.stonedata.formats.text.TextEncoder;
import org.stonedata.io.standard.AppendableOutput;
import org.stonedata.io.standard.SequenceInput;
import org.stonedata.references.impl.StandardReferenceProvider;
import org.stonedata.references.impl.DefaultReferenceTracker;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SampleUtils {

    private static Object decode(String text) {
        var decoder = new TextDecoder(new DefaultReferenceTracker());

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
