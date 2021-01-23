package org.stonedata;

import org.stonedata.errors.StoneException;
import org.stonedata.examiners.Examiner;
import org.stonedata.examiners.standard.StandardExaminers;
import org.stonedata.producers.Producer;
import org.stonedata.producers.standard.StandardArrayProducers;
import org.stonedata.producers.standard.StandardObjectProducers;
import org.stonedata.producers.standard.StandardValueProducers;
import org.stonedata.repositories.standard.StandardExaminerRepository;
import org.stonedata.repositories.standard.StandardProducerRepository;
import org.stonedata.util.ReflectUtils;

import java.lang.reflect.Type;
import java.util.function.Predicate;

public class StoneBuilder {

    private StandardProducerRepository producers;
    private StandardExaminerRepository examiners;

    private boolean skipNullFieldsValue;
    private boolean useCleanDefaultTypesValue;

    public StoneBuilder withObject(Class<?> type) {
        return withObject(type, ReflectUtils.computeDefaultTypeName(type));
    }

    public StoneBuilder withObject(Class<?> type, String name) {
        return withType(type, name,
                StandardExaminers.createObject(type, name),
                StandardObjectProducers.create(type, name));
    }

    public StoneBuilder withArray(Class<?> type) {
        return withArray(type, ReflectUtils.computeDefaultTypeName(type));
    }

    public StoneBuilder withArray(Class<?> type, String name) {
        return withType(type, name,
                StandardExaminers.createArray(type, name),
                StandardArrayProducers.create(type, name));
    }

    public StoneBuilder withValue(Class<?> type) {
        return withValue(type,
                ReflectUtils.computeDefaultTypeName(type));
    }

    public StoneBuilder withValue(Class<?> type, String name) {
        return withType(type, name,
                StandardExaminers.createValue(type, name),
                StandardValueProducers.create(type, name));
    }

    public StoneBuilder withType(Class<?> type, Examiner examiner) {
        return withType(type,
                ReflectUtils.computeDefaultTypeName(type),
                examiner, null);
    }

    public StoneBuilder withType(Class<?> type, Examiner examiner, Producer producer) {
        return withType(type,
                ReflectUtils.computeDefaultTypeName(type),
                examiner, producer);
    }

    public StoneBuilder withType(Class<?> type, Producer producer) {
        return withType(type,
                ReflectUtils.computeDefaultTypeName(type),
                null, producer);
    }

    public StoneBuilder withType(Class<?> type, String name, Producer producer) {
        return withType(type, name, null, producer);
    }

    public StoneBuilder withType(Class<?> type, String name, Examiner examiner, Producer producer) {
        if (producer == null && examiner == null) {
            throw new StoneException("Missing examiner or producer");
        }

        if (producer != null) {
            getProducers()
                    .register(name, producer)
                    .register(type, producer);
        }

        if (examiner != null) {
            getExaminers().register(examiner, type);
        }

        return this;
    }

    public StoneBuilder withExaminer(Examiner examiner, Predicate<Object> condition) {
        getExaminers().register(examiner, condition);
        return this;
    }

    public StoneBuilder withExaminer(Examiner examiner, Class<?> type) {
        getExaminers().register(examiner, type);
        return this;
    }

    public StoneBuilder withProducer(String name, Producer producer) {
        getProducers().register(name, producer);
        return this;
    }

    public StoneBuilder withProducer(Type type, Producer producer) {
        return withProducer(type,
                ReflectUtils.computeDefaultTypeName(type),
                producer);
    }

    public StoneBuilder withProducer(Type type, String name, Producer producer) {
        getProducers()
                .register(name, producer)
                .register(type, producer);
        return this;
    }

    public StoneBuilder skipNullFields(boolean value) {
        skipNullFieldsValue = value;
        return this;
    }

    public StoneBuilder useCleanDefaultTypes(boolean value) {
        useCleanDefaultTypesValue = value;
        return this;
    }

    private StandardProducerRepository getProducers() {
        if (producers == null) {
            producers = new StandardProducerRepository();
        }
        return producers;
    }

    private StandardExaminerRepository getExaminers() {
        if (examiners == null) {
            examiners = new StandardExaminerRepository();
        }
        return examiners;
    }

    public Stone build() {
        var stone = new Stone();
        stone.setExaminerRepository(examiners);
        stone.setProducerRepository(producers);
        stone.setSkipNullFields(skipNullFieldsValue);
        stone.setUseCleanDefaultTypes(useCleanDefaultTypesValue);
        return stone;
    }

}
