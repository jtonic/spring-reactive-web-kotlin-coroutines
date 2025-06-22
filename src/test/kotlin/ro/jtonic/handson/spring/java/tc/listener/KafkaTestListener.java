package ro.jtonic.handson.spring.java.tc.listener;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ro.jtonic.handson.spring.java.tc.model.TestMessage;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class KafkaTestListener {

    @Getter
    private CountDownLatch latch = new CountDownLatch(1);

    @Getter
    private TestMessage receivedMessage;

    @KafkaListener(topics = "${test.topic}")
    public void listen(TestMessage message) {
        log.info("Received message: {}", message);
        this.receivedMessage = message;
        latch.countDown();
    }

    public void resetLatch() {
        latch = new CountDownLatch(1);
    }

    public boolean awaitMessage(long timeout, TimeUnit unit) throws InterruptedException {
        return latch.await(timeout, unit);
    }
}
