package br.edu.fatecourinhos.thread.concurrent;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class Process<T> implements Runnable {
    @Getter
    @Setter
    private String name;
    private Thread currentThread = null;
    @Getter
    @Setter
    private ProcessStatus status = ProcessStatus.READY;
    private boolean finishedProcess = false;
    @Getter
    private Throwable throwable;
    @Getter
    private T processReturn;
    @Setter
    private boolean runAsVirtualThread = false;

    public Process(String threadName) {
        this.name = threadName;
    }

    public abstract T process();

    @Override
    public void run() {
        status = ProcessStatus.RUNNING;
        try {
            processReturn = process();
            status = ProcessStatus.SUCCESS;
        } catch (Exception e) {
            log.error(e.getMessage());
            status = ProcessStatus.ERROR;
        }

        finishedProcess = true;
        currentThread = null;
    }

    public void processSynchronous() {
        run();
    }

    public synchronized void processAssynchronous() {
        this.status = ProcessStatus.RUNNING;
        Thread.Builder builder = runAsVirtualThread ? Thread.ofVirtual() : Thread.ofPlatform();
        this.currentThread = builder.name(name).start(this);
    }

    public synchronized boolean isFinished(){
        return finishedProcess;
    }

    public synchronized boolean isRunning(){
        return !finishedProcess;
    }

    public synchronized void stop() {
        if (!finishedProcess) {
            this.currentThread.interrupt();
            this.currentThread = null;
        }
    }

    @SneakyThrows
    public void sleep(long milliseconds) {
        Thread.sleep(milliseconds);
    }

}
