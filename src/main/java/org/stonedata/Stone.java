package org.stonedata;

import org.stonedata.formats.text.TextDecoder;
import org.stonedata.io.CharInput;
import org.stonedata.io.standard.*;
import org.stonedata.formats.text.TextEncoder;
import org.stonedata.repositories.ExaminerRepository;
import org.stonedata.repositories.ProducerRepository;

import java.io.Reader;

public class Stone {

    public static StoneBuilder builder() {
        return new StoneBuilder();
    }

    private ProducerRepository producerRepository;
    private ExaminerRepository examinerRepository;
    private boolean skipNullFields;
    private boolean useCleanDefaultTypes;

    public TextDecoder newTextDecoder() {
        var decoder = new TextDecoder(producerRepository);

        decoder.setUseCleanDefaultTypes(useCleanDefaultTypes);

        return decoder;
    }

    public TextEncoder newTextEncoder() {
        var encoder = new TextEncoder(examinerRepository);

        encoder.setSkipNullFields(skipNullFields);

        return encoder;
    }

    public Object readText(CharSequence text) {
        return newTextDecoder().read(text);
    }

    public Object readText(CharInput input) {
        return newTextDecoder().read(input);
    }

    public Object readText(Reader reader) {
        return newTextDecoder().read(new ReaderInput(reader));
    }

    public <T> T readText(CharSequence text, Class<T> typeClass) {
        return newTextDecoder().read(text, typeClass);
    }

    public <T> T readText(CharInput input, Class<T> typeClass) {
        return newTextDecoder().read(input, typeClass);
    }

    public <T> T readText(Reader reader, Class<T> typeClass) {
        return newTextDecoder().read(new ReaderInput(reader), typeClass);
    }

    public void writeText(Object value, Appendable appendable) {
        newTextEncoder().write(value, new AppendableOutput(appendable));
    }

    public void writeText(Object value, Appendable appendable, boolean prettyPrint) {
        if (prettyPrint) {
            newTextEncoder().write(value, new PrettyPrintOutput(appendable));
        }
        else {
            writeText(value, appendable);
        }
    }

    public ProducerRepository getProducerRepository() {
        return producerRepository;
    }

    public void setProducerRepository(ProducerRepository producerRepository) {
        this.producerRepository = producerRepository;
    }

    public ExaminerRepository getExaminerRepository() {
        return examinerRepository;
    }

    public void setExaminerRepository(ExaminerRepository examinerRepository) {
        this.examinerRepository = examinerRepository;
    }

    public boolean getSkipNullFields() {
        return skipNullFields;
    }

    public void setSkipNullFields(boolean skipNullFields) {
        this.skipNullFields = skipNullFields;
    }

    public boolean getUseCleanDefaultTypes() {
        return useCleanDefaultTypes;
    }

    public void setUseCleanDefaultTypes(boolean useCleanDefaultTypes) {
        this.useCleanDefaultTypes = useCleanDefaultTypes;
    }

}
