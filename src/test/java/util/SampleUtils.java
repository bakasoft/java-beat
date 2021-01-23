package util;

import org.stonedata.coding.text.StoneTextDecoder;
import org.stonedata.coding.text.StoneTextEncoder;
import org.stonedata.io.impl.AppendableOutput;
import org.stonedata.io.impl.SequenceInput;
import org.stonedata.references.impl.StandardReferenceProvider;
import org.stonedata.references.impl.DefaultReferenceTracker;
import org.stonedata.repositories.standard.NullExaminerRepository;
import org.stonedata.repositories.standard.NullProducerRepository;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SampleUtils {

    private static Object decode(String text) throws IOException {
        var decoder = new StoneTextDecoder(NullProducerRepository.INSTANCE, new DefaultReferenceTracker());

        return decoder.read(new SequenceInput(text));
    }

    private static String encode(Object value) throws IOException {
        var encoder = new StoneTextEncoder(NullExaminerRepository.INSTANCE, new StandardReferenceProvider());
        var outputBuffer = new StringBuilder();

        encoder.write(value, new AppendableOutput(outputBuffer));

        return outputBuffer.toString();
    }

    public static void assertLosslessIO(String text) throws IOException {
        var obj0 = decode(text);
        var out0 = encode(obj0);
        var obj1 = decode(out0);
        var out1 = encode(obj1);

        assertEquals(obj0, obj1);

        CustomAssertions.assertSameText(out0, out1);
    }

}
