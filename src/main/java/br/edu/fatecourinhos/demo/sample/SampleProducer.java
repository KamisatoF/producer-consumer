package br.edu.fatecourinhos.demo.sample;

import br.edu.fatecourinhos.demo.concurrent.Producer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.BlockingQueue;

@Component
@Lazy
@Slf4j
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SampleProducer extends Producer<SampleItem> {

    public SampleProducer(BlockingQueue<SampleItem> blockingQueue) {
        super(blockingQueue);
    }

    @Override
    @SneakyThrows
    public void produce() {
        Thread.sleep(1L);
        for (int i = 0; i < 10; i++) {
            var someItem = new SampleItem((long) i, LocalDate.now(), BigDecimal.TEN);
            log.info("Produzido: " + i);
            putInQueue(someItem);
        }
    }
}
