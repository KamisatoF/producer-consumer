package br.edu.fatecourinhos.thread.concurrent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class Producer<T> extends Process<Void> {
    private static final int ONE_MB = 1024 * 1024;
    private final BlockingQueue<T> queue;
    private boolean keepRunning = true;
    @Value("${producer.offer.time:50}")
    private long timeOffer;
    @Value("${producer.offer.timeout:10000}")
    private long timeout;
    boolean showWarning = true;
    @Value("${producer.offer.memoryFreeBeforePutQueue:4}")
    private long memoryFreeBeforePutQueue;

    public Producer(BlockingQueue<T> queue) {
        super(Producer.class.getSimpleName());
        this.queue = queue;
    }

    public abstract void produce();

    @Override
    public Void process() {
        produce();
        return null;
    }

    public void putInQueue(T object) {
        while (keepRunning) {
            if (hasMemoryAvaliable()) {
                try {
                    boolean success = false;
                    long init = System.currentTimeMillis();
                    while (!success && keepRunning) {
                        success = queue.offer(object, timeOffer, TimeUnit.MILLISECONDS);
                        if (!success && System.currentTimeMillis() - init > timeout) {
                            throw new RuntimeException("Timeout exceeded while inserting into queue.");
                        }
                    }

                    if (success) break;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            } else {
                if (showWarning) {
                    log.warn("JVM has no memory avaliable.");
                    showWarning = false;
                }
                sleep(2000);
            }
        }
    }

    private boolean hasMemoryAvaliable() {
        long unusedMemory = Runtime.getRuntime().freeMemory();
        long usedMemory = Runtime.getRuntime().totalMemory() - unusedMemory;
        long memoryLimit = Runtime.getRuntime().maxMemory();

        return (memoryLimit - usedMemory) >= (memoryFreeBeforePutQueue * ONE_MB);
    }

    @Override
    public synchronized void stop() {
        keepRunning = false;
    }
}
