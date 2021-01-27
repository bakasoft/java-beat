package org.beat;

import org.beat.errors.BeatException;
import org.beat.examiners.Examiner;
import org.beat.examiners.standard.StandardExaminers;
import org.beat.producers.Producer;
import org.beat.producers.standard.StandardArrayProducers;
import org.beat.producers.standard.StandardObjectProducers;
import org.beat.producers.standard.StandardValueProducers;
import org.beat.references.ReferenceProvider;
import org.beat.references.ReferenceTracker;
import org.beat.references.impl.StandardReferenceProvider;
import org.beat.references.impl.StandardReferenceTracker;
import org.beat.repositories.standard.StandardExaminerRepository;
import org.beat.repositories.standard.StandardProducerRepository;
import org.beat.util.ReflectUtils;

import java.lang.reflect.Type;
import java.util.function.Function;
import java.util.function.Predicate;

public class BeatBuilder {

    private StandardProducerRepository producers;
    private StandardExaminerRepository examiners;
    private StandardReferenceProvider referenceProvider;
    private StandardReferenceTracker referenceTracker;

    private boolean skipNullFieldsValue;
    private boolean useCleanDefaultTypesValue;

    public BeatBuilder withObject(Class<?> type) {
        return withObject(type, ReflectUtils.computeDefaultTypeName(type));
    }

    public BeatBuilder withObject(Class<?> type, String name) {
        return withType(type, name,
                StandardExaminers.createObject(type, name),
                StandardObjectProducers.create(type, name));
    }

    public BeatBuilder withArray(Class<?> type) {
        return withArray(type, ReflectUtils.computeDefaultTypeName(type));
    }

    public BeatBuilder withArray(Class<?> type, String name) {
        return withType(type, name,
                StandardExaminers.createArray(type, name),
                StandardArrayProducers.create(type, name));
    }

    public BeatBuilder withValue(Class<?> type) {
        return withValue(type,
                ReflectUtils.computeDefaultTypeName(type));
    }

    public BeatBuilder withValue(Class<?> type, String name) {
        return withType(type, name,
                StandardExaminers.createValue(type, name),
                StandardValueProducers.create(type, name));
    }

    public BeatBuilder withType(Class<?> type, Examiner examiner) {
        return withType(type,
                ReflectUtils.computeDefaultTypeName(type),
                examiner, null);
    }

    public BeatBuilder withType(Class<?> type, Examiner examiner, Producer producer) {
        return withType(type,
                ReflectUtils.computeDefaultTypeName(type),
                examiner, producer);
    }

    public BeatBuilder withType(Class<?> type, Producer producer) {
        return withType(type,
                ReflectUtils.computeDefaultTypeName(type),
                null, producer);
    }

    public BeatBuilder withType(Class<?> type, String name, Producer producer) {
        return withType(type, name, null, producer);
    }

    public BeatBuilder withType(Class<?> type, String name, Examiner examiner, Producer producer) {
        if (producer == null && examiner == null) {
            throw new BeatException("Missing examiner or producer");
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

    public BeatBuilder withExaminer(Examiner examiner, Predicate<Object> condition) {
        getExaminers().register(examiner, condition);
        return this;
    }

    public BeatBuilder withExaminer(Examiner examiner, Class<?> type) {
        getExaminers().register(examiner, type);
        return this;
    }

    public BeatBuilder withProducer(String name, Producer producer) {
        getProducers().register(name, producer);
        return this;
    }

    public BeatBuilder withProducer(Type type, Producer producer) {
        return withProducer(type,
                ReflectUtils.computeDefaultTypeName(type),
                producer);
    }

    public BeatBuilder withProducer(Type type, String name, Producer producer) {
        getProducers()
                .register(name, producer)
                .register(type, producer);
        return this;
    }

    public BeatBuilder skipNullFields(boolean value) {
        skipNullFieldsValue = value;
        return this;
    }

    public BeatBuilder useCleanDefaultTypes(boolean value) {
        useCleanDefaultTypesValue = value;
        return this;
    }

    public BeatBuilder withValueReference(Object value, String reference) {
        if (referenceProvider == null) {
            referenceProvider = new StandardReferenceProvider();
        }
        if (referenceTracker == null) {
            referenceTracker = new StandardReferenceTracker();
        }
        referenceProvider.setReference(value, reference);
        referenceTracker.store(reference, value);
        return this;
    }

    public BeatBuilder withReferenceGenerator(Function<Object, String> generator) {
        if (referenceProvider == null) {
            referenceProvider = new StandardReferenceProvider();
        }
        referenceProvider.addGenerator(generator);
        return this;
    }

    public <T> BeatBuilder withReferenceGenerator(Class<T> typeClass, Function<T, String> generator) {
        return withReferenceGenerator(obj -> {
            if (typeClass.isInstance(obj)) {
                return generator.apply(typeClass.cast(obj));
            }
            return null;
        });
    }

    public BeatBuilder withHashReference(Class<?> typeClass) {
        return withReferenceGenerator(obj -> {
            if (typeClass.isInstance(obj)) {
                return Integer.toHexString(typeClass.hashCode()) + Integer.toHexString(obj.hashCode());
            }
            return null;
        });
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

    public Beat build() {
        var beat = new Beat();
        beat.setExaminerRepository(examiners);
        beat.setProducerRepository(producers);
        beat.setSkipNullFields(skipNullFieldsValue);
        beat.setUseCleanDefaultTypes(useCleanDefaultTypesValue);
        beat.setReferenceProvider(referenceProvider);
        beat.setReferenceTracker(referenceTracker);
        return beat;
    }

}
