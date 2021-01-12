package org.stonedata;

import org.stonedata.coding.text.StoneTextDecoder;
import org.stonedata.errors.MissingInputException;
import org.stonedata.errors.StoneException;
import org.stonedata.examiners.ExaminerRepository;
import org.stonedata.examiners.impl.EmptyExaminerRepository;
import org.stonedata.io.StoneCharInput;
import org.stonedata.io.StoneCharOutput;
import org.stonedata.io.impl.*;
import org.stonedata.coding.text.StoneTextEncoder;
import org.stonedata.producers.impl.ClassObjectProducer;
import org.stonedata.producers.impl.EmptyProducerRepository;
import org.stonedata.schema.StoneSchema;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Pattern;

public class Stone {

    Pattern TOKEN_PATTERN = Pattern.compile("[a-zA-Z0-9_./+-]+");

    public Stone() {

    }

    static StoneCharOutput standardOutput(Appendable output) {
        return new StoneAppendableOutput(output);
    }

    static StoneCharOutput prettyOutput(Appendable output) {
        return new PrettyPrintOutput(output);
    }

    static StoneTextEncoder standardEncoder(StoneSchema schema) {
        return standardEncoder(schema.createExaminerRepository());
    }

    static StoneTextEncoder standardEncoder(ExaminerRepository repository) {
        return new StoneTextEncoder(repository);
    }

    static StoneCharInput charInput(CharSequence sequence) {
        return new SequenceInput(sequence);
    }

    static StoneCharInput charInput(Readable readable) {
        return new ReadableInput(readable);
    }

    public <T> T readText(URL url, Class<T> type) throws IOException {
        if (url == null) {
            throw new MissingInputException();
        }

        try (var stream = new InputStreamReader(url.openStream())) {
            var input = new ReaderInput(stream);

            return read(input, type);
        }
    }

    public <T> T read(String text, Class<T> type) throws IOException {
        return read(new SequenceInput(text), type);
    }

    public <T> T read(StoneCharInput input, Class<T> type) throws IOException {
        if (input == null) {
            throw new MissingInputException();
        }

        var repository = new EmptyProducerRepository();
        var decoder = new StoneTextDecoder(repository);
        var producer = new ClassObjectProducer(type);
        var result = decoder.readObject(input, producer, null);
        return type.cast(result);
    }

    public void write(Object value, Appendable appendable) throws IOException {
        write(value, new StoneAppendableOutput(appendable));
    }

    public void write(Object value, Appendable appendable, boolean prettyPrint) throws IOException {
        write(value, prettyPrint ? new PrettyPrintOutput(appendable) : new StoneAppendableOutput(appendable));
    }

    public void write(Object value, StoneCharOutput output) throws IOException {
        var repository = new EmptyExaminerRepository();
        var encoder = new StoneTextEncoder(repository);

        encoder.write(value, output);
    }

}
