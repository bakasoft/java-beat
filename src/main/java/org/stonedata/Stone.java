package org.stonedata;

import org.stonedata.formats.text.TextDecoder;
import org.stonedata.errors.MissingInputException;
import org.stonedata.references.impl.NullReferenceProvider;
import org.stonedata.io.CharInput;
import org.stonedata.io.CharOutput;
import org.stonedata.io.standard.*;
import org.stonedata.formats.text.TextEncoder;
import org.stonedata.references.impl.DefaultReferenceTracker;
import org.stonedata.repositories.ExaminerRepository;
import org.stonedata.repositories.ProducerRepository;

import java.io.IOException;

public class Stone {

    private final ProducerRepository producerRepository;
    private final ExaminerRepository examinerRepository;
    private final boolean skipEncodingNullFields;

    public Stone(ProducerRepository producerRepository, ExaminerRepository examinerRepository, boolean skipEncodingNullFields) {
        this.producerRepository = producerRepository;
        this.examinerRepository = examinerRepository;
        this.skipEncodingNullFields = skipEncodingNullFields;
    }

    public <T> T readText(String text, Class<T> type) throws IOException {
        return readText(new SequenceInput(text), type);
    }

    public <T> T readText(CharInput input, Class<T> type) throws IOException {
        if (producerRepository == null) {
            throw new RuntimeException();
        }
        return readText(input, type, producerRepository);
    }

    public static <T> T readText(CharInput input, Class<T> type, ProducerRepository repository) throws IOException {
        if (input == null) {
            throw new MissingInputException();
        }
        var references = new DefaultReferenceTracker();
        var decoder = new TextDecoder(repository, references);
        var result = decoder.read(input, type);
        return type.cast(result);
    }

    public Object readText(String input) throws IOException {
        return readText(new SequenceInput(input));
    }

    public Object readText(CharInput input) throws IOException {
        if (producerRepository == null) {
            throw new RuntimeException();
        }
        return readText(input, producerRepository);
    }

    public static Object readText(CharInput input, ProducerRepository repository) throws IOException {
        if (input == null) {
            throw new MissingInputException();
        }
        var references = new DefaultReferenceTracker();
        var decoder = new TextDecoder(repository, references);
        return decoder.read(input);
    }

    public void writeText(Object value, Appendable appendable) throws IOException {
        writeText(value, new AppendableOutput(appendable));
    }

    public void writeText(Object value, Appendable appendable, boolean prettyPrint) throws IOException {
        writeText(value, prettyPrint ? new PrettyPrintOutput(appendable) : new AppendableOutput(appendable));
    }

    public void writeText(Object value, CharOutput output) throws IOException {
        if (examinerRepository == null) {
            throw new RuntimeException();
        }
        var encoder = new TextEncoder(examinerRepository, NullReferenceProvider.INSTANCE);

        encoder.setSkipNullFields(skipEncodingNullFields);

        encoder.write(value, output);
    }

    public boolean isSkipEncodingNullFields() {
        return skipEncodingNullFields;
    }

    public static StoneBuilder builder() {
        return new StoneBuilder();
    }

}
