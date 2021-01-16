package org.stonedata;

import org.stonedata.coding.text.StoneTextDecoder;
import org.stonedata.errors.MissingInputException;
import org.stonedata.examiners.Examiner;
import org.stonedata.producers.standard.StandardArrayProducers;
import org.stonedata.producers.standard.StandardObjectProducers;
import org.stonedata.producers.standard.StandardValueProducers;
import org.stonedata.producers.standard.array.SoftTypedListProducer;
import org.stonedata.producers.standard.object.SoftTypedObjectProducer;
import org.stonedata.producers.standard.value.SoftTypedValueProducer;
import org.stonedata.references.impl.NullReferenceProvider;
import org.stonedata.repositories.ExaminerRepository;
import org.stonedata.examiners.ValueExaminer;
import org.stonedata.examiners.standard.StandardExaminers;
import org.stonedata.repositories.standard.StoneExaminerRepository;
import org.stonedata.io.StoneCharInput;
import org.stonedata.io.StoneCharOutput;
import org.stonedata.io.impl.*;
import org.stonedata.coding.text.StoneTextEncoder;
import org.stonedata.producers.Producer;
import org.stonedata.repositories.ProducerRepository;
import org.stonedata.producers.ValueProducer;
import org.stonedata.repositories.standard.StoneProducerRepository;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Stone {

    private Map<Class<?>, Examiner> examiners;
    private Map<String, Producer> producers;
    private ExaminerRepository examinerRepository;
    private ProducerRepository producerRepository;

    private boolean skipEncodingNullFields;

    public Stone() {}

    public void registerObject(Class<?> type) {
        registerExaminer(type);
        registerObjectProducer(type);
    }

    public void registerObject(Class<?> type, String name) {
        registerExaminer(type, name);
        registerObjectProducer(type, name);
    }

    public void registerArray(Class<?> type) {
        registerExaminer(type);
        registerArrayProducer(type);
    }

    public void registerArray(Class<?> type, String name) {
        registerExaminer(type, name);
        registerArrayProducer(type, name);
    }

    public void registerValue(Class<?> type) {
        registerExaminer(type);
        registerValueProducer(type);
    }

    public void registerValue(Class<?> type, String name) {
        registerExaminer(type, name);
        registerValueProducer(type, name);
    }

    public Examiner getExaminer(Class<?> type) {
        if (examiners == null) {
            return null;
        }
        return examiners.get(type);
    }

    public void registerExaminer(Class<?> type) {
        registerExaminer(type, type.getSimpleName());
    }

    public void registerExaminer(Class<?> type, String name) {
        registerExaminer(type, StandardExaminers.createExaminer(type, name));
    }

    public void registerExaminer(Class<?> type, Examiner examiner) {
        var map = getExaminers();
        if (map.containsKey(type)) {
            throw new RuntimeException();
        }
        map.put(type, examiner);
    }

    public Map<Class<?>, Examiner> getExaminers() {
        if (examiners == null) {
            examiners = new LinkedHashMap<>();
        }
        return examiners;
    }

    public void setExaminers(Map<Class<?>, Examiner> examiners) {
        this.examiners = examiners;
    }

    public Producer getProducer(String name) {
        if (producers == null) {
            return null;
        }
        return producers.get(name);
    }

    public void registerObjectProducer(Class<?> type) {
        registerObjectProducer(type, type.getSimpleName());
    }

    public void registerArrayProducer(Class<?> type) {
        registerArrayProducer(type, type.getSimpleName());
    }

    public void registerValueProducer(Class<?> type) {
        registerValueProducer(type, type.getSimpleName());
    }

    public <T> void registerValueProducer(Class<T> type, Function<Object, T> maker) {
        ValueProducer producer = (args) -> {
            if (args.size() != 1) {
                throw new RuntimeException();
            }

            return maker.apply(args.get(0));
        };

        registerProducer(type.getSimpleName(), producer);
    }

    public <T> void registerValueProducer(Class<T> type, BiFunction<Object, Object, T> maker) {
        ValueProducer producer = (args) -> {
            if (args.size() != 2) {
                throw new RuntimeException();
            }

            return maker.apply(args.get(0), args.get(1));
        };

        registerProducer(type.getSimpleName(), producer);
    }

    public <T> void registerValueExaminer(Class<T> type, Function<T, List<Object>> encoder) {
        registerExaminer(type, new ValueExaminer() {
            @Override
            public List<Object> computeArguments(Object value) {
                var tobj = type.cast(value);

                return encoder.apply(tobj);
            }

            @Override
            public String getType() {
                return type.getSimpleName();
            }
        });
    }

    public void registerObjectProducer(Class<?> type, String name) {
        var producer = StandardObjectProducers.tryCreate(type);
        if (producer == null) {
            producer = new SoftTypedObjectProducer(type.getSimpleName());
        }
        registerProducer(name, producer);
    }

    public void registerArrayProducer(Class<?> type, String name) {
        var producer = StandardArrayProducers.tryCreate(type);
        if (producer == null) {
            producer = new SoftTypedListProducer(type.getSimpleName());
        }
        registerProducer(name, producer);
    }

    public void registerValueProducer(Class<?> type, String name) {
        var producer = StandardValueProducers.tryCreate(type);
        if (producer == null) {
            producer = new SoftTypedValueProducer(type.getSimpleName());
        }
        registerProducer(name, producer);
    }

    public void registerProducer(String name, Producer producer) {
        var map = getProducers();
        if (map.containsKey(name)) {
            throw new RuntimeException();
        }
        map.put(name, producer);
    }

    public Map<String, Producer> getProducers() {
        if (producers == null) {
            producers = new LinkedHashMap<>();
        }
        return producers;
    }

    public void setProducers(Map<String, Producer> producers) {
        this.producers = producers;
    }

    public ExaminerRepository getExaminerRepository() {
        if (examinerRepository == null) {
            examinerRepository = new StoneExaminerRepository(this);
        }

        return examinerRepository;
    }

    public void setExaminerRepository(ExaminerRepository repository) {
        this.examinerRepository = repository;
    }

    public ProducerRepository getProducerRepository() {
        if (producerRepository == null) {
            producerRepository = new StoneProducerRepository(this);
        }

        return producerRepository;
    }

    public void setProducerRepository(ProducerRepository repository) {
        this.producerRepository = repository;
    }

    public <T> T readText(String text, Class<T> type) throws IOException {
        return readText(new SequenceInput(text), type);
    }

    public <T> T readText(StoneCharInput input, Class<T> type) throws IOException {
        var repository = getProducerRepository();
        return readText(input, type, repository);
    }

    public static <T> T readText(StoneCharInput input, Class<T> type, ProducerRepository repository) throws IOException {
        if (input == null) {
            throw new MissingInputException();
        }
        var decoder = new StoneTextDecoder(repository);
        var result = decoder.read(input, type);
        return type.cast(result);
    }

    public Object readText(String input) throws IOException {
        return readText(new SequenceInput(input));
    }

    public Object readText(StoneCharInput input) throws IOException {
        var repository = getProducerRepository();
        return readText(input, repository);
    }

    public static Object readText(StoneCharInput input, ProducerRepository repository) throws IOException {
        if (input == null) {
            throw new MissingInputException();
        }
        var decoder = new StoneTextDecoder(repository);
        return decoder.read(input);
    }

    public void writeText(Object value, Appendable appendable) throws IOException {
        writeText(value, new AppendableOutput(appendable));
    }

    public void writeText(Object value, Appendable appendable, boolean prettyPrint) throws IOException {
        writeText(value, prettyPrint ? new PrettyPrintOutput(appendable) : new AppendableOutput(appendable));
    }

    public void writeText(Object value, StoneCharOutput output) throws IOException {
        var repository = getExaminerRepository();
        var encoder = new StoneTextEncoder(repository, NullReferenceProvider.INSTANCE);

        encoder.setSkipNullFields(skipEncodingNullFields);

        encoder.write(output, value);
    }

    public boolean isSkipEncodingNullFields() {
        return skipEncodingNullFields;
    }

    public void setSkipEncodingNullFields(boolean skipEncodingNullFields) {
        this.skipEncodingNullFields = skipEncodingNullFields;
    }
}
