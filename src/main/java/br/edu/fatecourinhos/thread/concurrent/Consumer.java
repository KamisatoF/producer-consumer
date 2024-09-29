package br.edu.fatecourinhos.thread.concurrent;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
@Setter
public abstract class Consumer<T> extends Process<Void> {
    private boolean producerIsFinished = false;
    private final BlockingQueue<T> blockingQueue;
    private boolean keepRunning = true;
    @Value("${consumer.poll.timeout:10000}")
    private long timeout;
    @Value("${consumer.poll.time:1000}")
    private long timePool;

    public Consumer(BlockingQueue<T> blockingQueue) {
        super(Consumer.class.getSimpleName());
        this.blockingQueue = blockingQueue;
    }

    public abstract void consume(T e);

    @Override
    public Void process() {
        long init = System.currentTimeMillis();
        while (keepRunning) {
            try {
                T pool = blockingQueue.poll(timePool, TimeUnit.MILLISECONDS);
                if (pool != null) {
                    consume(pool);
                    init = System.currentTimeMillis();
                } else {
                    if (producerIsFinished) break;
                    if (System.currentTimeMillis() - init > timeout) throw new RuntimeException("Timeout exceeded when polling the queue");
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        afterConsumption();
        return null;
    }

    public void afterConsumption() {}

    public synchronized void producerIsFinished() {
        producerIsFinished = true;
    }

    @Override
    public synchronized void stop() {
        keepRunning = false;
    }
}

