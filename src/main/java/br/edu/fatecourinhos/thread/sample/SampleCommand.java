package br.edu.fatecourinhos.thread.sample;

import br.edu.fatecourinhos.thread.concurrent.ProducerConsumerFactory;
import br.edu.fatecourinhos.thread.concurrent.ProducerConsumerRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SampleCommand {
    private final ProducerConsumerFactory factory;

    public SampleCommand(ProducerConsumerFactory factory) {
        this.factory = factory;
    }

    public Integer run(){
        var queue = factory.createQueue(2);
        List<SampleProducer> producers = factory.generateProducerPool(1, SampleProducer.class, queue);
        List<SampleConsumer> consumers = factory.generateConsumerPool(5, SampleConsumer.class, queue);

        var runner = new ProducerConsumerRunner(producers, consumers, true);
        return runner.run();
    }
}
