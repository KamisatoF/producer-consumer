package br.edu.fatecourinhos.thread.sample;

import br.edu.fatecourinhos.thread.concurrent.Consumer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
@Lazy
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Slf4j
public class SampleConsumer extends Consumer<SampleItem> {
    public SampleConsumer(BlockingQueue<SampleItem> blockingQueue) {
        super(blockingQueue);
    }

    @Override
    @SneakyThrows
    public void consume(SampleItem e) {
        Thread.sleep(1L);
        log.info("Consumindo: " + e.id());
    }
}
