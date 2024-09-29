package br.edu.fatecourinhos.thread.concurrent;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Component
public class ProducerConsumerFactory {
    public static final int DEFAULT_QUEUE_CAPACITY = 256;
    private final ApplicationContext context;

    public ProducerConsumerFactory(ApplicationContext context) {
        this.context = context;
    }

    public <T> List<T> generateProducerPool(int poolSize, Class<T> aClass, Object... args) {
        return generateProcess(poolSize, Producer.class.getSimpleName(), aClass, args);
    }

    public <T> List<T> generateConsumerPool(int poolSize, Class<T> aClass, Object... args) {
        return generateProcess(poolSize, Consumer.class.getSimpleName(), aClass, args);
    }

    public <T> List<T> generateProcess(int poolSize, String prefixName, Class<T> aClass, Object... args) {
        List<T> list = new ArrayList<>(poolSize);
        for (int i = 0; i < poolSize; i++) {
            T bean = context.getBean(aClass, args);
            ((Process<?>) bean).setName(prefixName + "_" + i);
            list.add(bean);
        }

        return list;
    }

    public <T> BlockingQueue<T> createQueue(int capacity) {
        return new ArrayBlockingQueue<>(capacity);
    }

    public <T> BlockingQueue<T> createQueue() {
        return createQueue(DEFAULT_QUEUE_CAPACITY);
    }
}
