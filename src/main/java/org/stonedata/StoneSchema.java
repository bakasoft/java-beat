package org.stonedata;

import org.stonedata.examiners.Examiner;
import org.stonedata.examiners.ExaminerRepository;
import org.stonedata.examiners.impl.GenericListExaminer;
import org.stonedata.examiners.impl.GenericMapExaminer;
import org.stonedata.examiners.impl.GenericObjectExaminer;
import org.stonedata.examiners.impl.StandardExaminerRepository;
import org.stonedata.producers.ProducerRepository;
import org.stonedata.producers.impl.StandardProducerRepository;
import org.stonedata.text.StoneTextDecoder;
import org.stonedata.text.StoneTextEncoder;

import java.util.*;

public class StoneSchema {

    private final Map<String, Class<?>> types;

    public StoneSchema() {
        this.types = new LinkedHashMap<>();
    }

    public void addType(Class<?> type) {
        addType(type.getSimpleName(), type);
    }

    public void addType(String name, Class<?> type) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(type);

        if (types.containsKey(name)) {
            throw new RuntimeException();
        }

        types.put(name, type);
    }

    public ProducerRepository createMakerRepository() {
        var makers = new StandardProducerRepository();

        for (var entry : types.entrySet()) {
            var name = entry.getKey();
            var type = entry.getValue();

            makers.addObjectMakerFor(type, name);
        }

        return makers;
    }

    public ExaminerRepository createExaminerRepository() {
        var repo = new StandardExaminerRepository();

        for (var entry : types.entrySet()) {
            var name = entry.getKey();
            var type = entry.getValue();
            var examiner = createExaminer(type, name);

            repo.addExaminer(type, examiner);
        }

        return repo;
    }

    private Examiner createExaminer(Class<?> type, String name) {
        if (List.class.isAssignableFrom(type)) {
            return new GenericListExaminer(name);
        }
        else if (Map.class.isAssignableFrom(type)) {
            return new GenericMapExaminer(name);
        }
        else {
            return new GenericObjectExaminer(type, name);
        }
    }

    public StoneTextEncoder createTextEncoder() {
        return new StoneTextEncoder(createExaminerRepository());
    }

    public StoneTextDecoder createTextDecoder() {
        return new StoneTextDecoder(createMakerRepository());
    }

}
