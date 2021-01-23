package org.stonedata;

import org.stonedata.examiners.Examiner;
import org.stonedata.examiners.standard.object.ClassObjectExaminer;
import org.stonedata.producers.Producer;
import org.stonedata.producers.standard.object.ClassObjectProducer;
import org.stonedata.repositories.standard.StandardExaminerRepository;
import org.stonedata.repositories.standard.StandardProducerRepository;
import org.stonedata.util.ReflectUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class StoneBuilder {

    private final List<StoneEntry> entries;

    private boolean skipEncodingNullFieldsValue;

    public StoneBuilder() {
        entries = new ArrayList<>();
    }

    public StoneBuilder withObject(Class<?> type) {
        return withObject(type, type.getSimpleName());
    }

    public StoneBuilder withObject(Class<?> type, String name) {
        entries.add(new ObjectTypeEntry(type, name));
        return this;
    }

    public StoneBuilder withArray(Class<?> type) {
        return withArray(type, type.getSimpleName());
    }

    public StoneBuilder withArray(Class<?> type, String name) {
        entries.add(new ArrayTypeEntry(type, name));
        return this;
    }

    public StoneBuilder withValue(Class<?> type) {
        return withValue(type, ReflectUtils.computeDefaultTypeName(type), null, null);
    }

    public StoneBuilder withValue(Class<?> type, Examiner examiner, Producer producer) {
        return withValue(type, ReflectUtils.computeDefaultTypeName(type), examiner, producer);
    }

    public StoneBuilder withValue(Class<?> type, String name) {
        return withValue(type, name, null, null);
    }

    public StoneBuilder withValue(Class<?> type, String name, Examiner examiner, Producer producer) {
        entries.add(new ValueTypeEntry(type, name));
        entries.add(new ExaminerForType(examiner, type));
        entries.add(new ProducerForType(producer, type));
        entries.add(new ProducerForName(producer, name));
        return this;
    }

    public StoneBuilder withExaminer(Examiner examiner, Predicate<Object> condition) {
        entries.add(new ExaminerForCondition(examiner, condition));
        return this;
    }

    public StoneBuilder withExaminer(Examiner examiner, Class<?> type) {
        entries.add(new ExaminerForType(examiner, type));
        return this;
    }

    public StoneBuilder withProducer(Producer producer, BiPredicate<String, Type> condition) {
        entries.add(new ProducerForCondition(producer, condition));
        return this;
    }

    public StoneBuilder withProducer(Producer producer, String name) {
        entries.add(new ProducerForName(producer, name));
        return this;
    }

    public StoneBuilder withProducer(Producer producer, Type type) {
        entries.add(new ProducerForType(producer, type));
        return this;
    }

    public StoneBuilder skipEncodingNullFields(boolean value) {
        skipEncodingNullFieldsValue = value;
        return this;
    }

    public Stone buildEncoder() {
        return build(true, false);
    }

    public Stone buildDecoder() {
        return build(false, true);
    }

    public Stone build() {
        return build(true, true);
    }

    public Stone build(boolean encoder, boolean decoder) {
        var producerRepository = encoder ? new StandardProducerRepository() : null;
        var examinerRepository = decoder ? new StandardExaminerRepository() : null;

        for (var entry : entries) {
            entry.build(producerRepository, examinerRepository);
        }

        return new Stone(producerRepository, examinerRepository, skipEncodingNullFieldsValue);
    }

    private interface StoneEntry {
        void build(StandardProducerRepository producerRepository, StandardExaminerRepository examinerRepository);
    }

    private static class ObjectTypeEntry implements StoneEntry {
        final Class<?> type;
        final String name;

        ObjectTypeEntry(Class<?> type, String name) {
            this.type = Objects.requireNonNull(type);
            this.name = Objects.requireNonNull(name);
        }

        @Override
        public void build(StandardProducerRepository producerRepository, StandardExaminerRepository examinerRepository) {
            if (producerRepository != null) {
                var producer = new ClassObjectProducer(type);

                producerRepository.register(name, producer);
                producerRepository.register(type, producer);
            }

            if (examinerRepository != null) {
                var examiner = new ClassObjectExaminer(type, name);

                examinerRepository.register(examiner, type);
            }
        }
    }

    private static class ArrayTypeEntry implements StoneEntry {
        final Class<?> type;
        final String name;

        ArrayTypeEntry(Class<?> type, String name) {
            this.type = type;
            this.name = name;
        }

        @Override
        public void build(StandardProducerRepository producerRepository, StandardExaminerRepository examinerRepository) {

        }
    }

    private static class ValueTypeEntry implements StoneEntry {
        final Class<?> type;
        final String name;

        ValueTypeEntry(Class<?> type, String name) {
            this.type = type;
            this.name = name;
        }

        @Override
        public void build(StandardProducerRepository producerRepository, StandardExaminerRepository examinerRepository) {

        }
    }

    private static class ExaminerForCondition implements StoneEntry {
        final Examiner examiner;
        final Predicate<Object> condition;

        ExaminerForCondition(Examiner examiner, Predicate<Object> condition) {
            this.examiner = examiner;
            this.condition = condition;
        }

        @Override
        public void build(StandardProducerRepository producerRepository, StandardExaminerRepository examinerRepository) {

        }
    }

    private static class ExaminerForType implements StoneEntry {
        final Examiner examiner;
        final Class<?> type;

        ExaminerForType(Examiner examiner, Class<?> type) {
            this.examiner = examiner;
            this.type = type;
        }

        @Override
        public void build(StandardProducerRepository producerRepository, StandardExaminerRepository examinerRepository) {
            if (examinerRepository != null) {
                examinerRepository.register(examiner, type);
            }
        }
    }

    private static class ProducerForCondition implements StoneEntry {
        final Producer producer;
        final BiPredicate<String, Type> condition;

        private ProducerForCondition(Producer producer, BiPredicate<String, Type> condition) {
            this.producer = producer;
            this.condition = condition;
        }

        @Override
        public void build(StandardProducerRepository producerRepository, StandardExaminerRepository examinerRepository) {

        }
    }

    private static class ProducerForName implements StoneEntry {
        final Producer producer;
        final String name;

        private ProducerForName(Producer producer, String name) {
            this.producer = producer;
            this.name = name;
        }

        @Override
        public void build(StandardProducerRepository producerRepository, StandardExaminerRepository examinerRepository) {

        }
    }

    private static class ProducerForType implements StoneEntry {
        final Producer producer;
        final Type type;

        private ProducerForType(Producer producer, Type type) {
            this.producer = producer;
            this.type = type;
        }

        @Override
        public void build(StandardProducerRepository producerRepository, StandardExaminerRepository examinerRepository) {
            if (producerRepository != null) {
                producerRepository.register(type, producer);

                var name = ReflectUtils.computeDefaultTypeName(type);
                if (!producerRepository.contains(name)) {
                    producerRepository.register(name, producer);
                }
            }
        }
    }

}
