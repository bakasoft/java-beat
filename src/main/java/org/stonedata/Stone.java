package org.stonedata;

import org.stonedata.coding.text.StoneTextDecoder;
import org.stonedata.errors.MissingInputException;
import org.stonedata.examiners.Examiner;
import org.stonedata.examiners.ExaminerRepository;
import org.stonedata.examiners.impl.DefaultExaminers;
import org.stonedata.examiners.impl.StandardExaminerRepository;
import org.stonedata.io.StoneCharInput;
import org.stonedata.io.StoneCharOutput;
import org.stonedata.io.impl.*;
import org.stonedata.coding.text.StoneTextEncoder;
import org.stonedata.producers.Producer;
import org.stonedata.producers.ProducerRepository;
import org.stonedata.producers.impl.ClassObjectProducer;
import org.stonedata.producers.impl.DefaultProducers;
import org.stonedata.producers.impl.StandardProducerRepository;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class Stone {

    private Map<Class<?>, Examiner> examiners;
    private Map<String, Producer> producers;
    private ExaminerRepository examinerRepository;
    private ProducerRepository producerRepository;

    public Stone() {}

    public void register(Class<?> type) {
        registerExaminer(type);
        registerProducer(type);
    }

    public void register(Class<?> type, String name) {
        registerExaminer(type, name);
        registerProducer(type, name);
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
        registerExaminer(type, DefaultExaminers.createExaminer(type, name));
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

    public void registerProducer(Class<?> type) {
        registerProducer(type, type.getSimpleName());
    }

    public void registerProducer(Class<?> type, String name) {
        registerProducer(name, DefaultProducers.createProducer(type));
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
            examinerRepository = new StandardExaminerRepository(this);
        }

        return examinerRepository;
    }

    public void setExaminerRepository(ExaminerRepository repository) {
        this.examinerRepository = repository;
    }

    public ProducerRepository getProducerRepository() {
        if (producerRepository == null) {
            producerRepository = new StandardProducerRepository(this);
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
        var producer = new ClassObjectProducer(type);
        var result = decoder.readObject(input, producer, null);
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
        writeText(value, new StoneAppendableOutput(appendable));
    }

    public void writeText(Object value, Appendable appendable, boolean prettyPrint) throws IOException {
        writeText(value, prettyPrint ? new PrettyPrintOutput(appendable) : new StoneAppendableOutput(appendable));
    }

    public void writeText(Object value, StoneCharOutput output) throws IOException {
        var repository = getExaminerRepository();
        var encoder = new StoneTextEncoder(repository);

        encoder.write(value, output);
    }

}
