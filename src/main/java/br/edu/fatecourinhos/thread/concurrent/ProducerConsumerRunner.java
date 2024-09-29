package br.edu.fatecourinhos.thread.concurrent;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ProducerConsumerRunner {
    private final List<? extends Producer<?>> producers;
    private final List<? extends Consumer<?>> consumers;
    private boolean runAsVirtualThreads = false;

    public ProducerConsumerRunner(List<? extends Producer<?>> producers, List<? extends Consumer<?>> consumers) {
        this.producers = producers;
        this.consumers = consumers;
    }

    public ProducerConsumerRunner(List<? extends Producer<?>> producers, List<? extends Consumer<?>> consumers, boolean runAsVirtualThreads) {
        this.producers = producers;
        this.consumers = consumers;
        this.runAsVirtualThreads = runAsVirtualThreads;
    }

    public Integer run() {
        try {
            log.info("Running with " + (runAsVirtualThreads ? "virtual" : "Ppataform") + " threads.");
            producers.forEach(this::runProcess);
            consumers.forEach(this::runProcess);

            while (isNotDone(producers)) {
                if (isError(consumers) || isError(producers)) {
                    stopAll();
                    break;
                }
                Thread.sleep(100);
            }
            throwsIfError(producers);

            consumers.forEach(Consumer::producerIsFinished);
            while (isNotDone(consumers)) Thread.sleep(100);

            throwsIfError(consumers);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return 0;
    }

    private void throwsIfError(List<? extends Process<?>> processes) {
        var name = processes.stream().findAny().getClass().getSimpleName();
        processes.stream().filter(producer -> producer.isFinished() && producer.getStatus() == ProcessStatus.ERROR).forEach(producer -> {
            throw new IllegalStateException(name + " finished with ERROR: " + producer.getName());
        });
    }

    private boolean isError(List<? extends Process<?>> processes) {
        return processes.stream().anyMatch(process -> process.getStatus() == ProcessStatus.ERROR);
    }

    private boolean isNotDone(List<? extends Process<?>> processes) {
        return processes.stream().anyMatch(process -> !process.isFinished());
    }

    private void runProcess(Process<?> c) {
        c.setRunAsVirtualThread(runAsVirtualThreads);
        c.processAssynchronous();
    }

    private synchronized void stopAll() {
        producers.forEach(Process::stop);
        consumers.forEach(Process::stop);
    }

}
