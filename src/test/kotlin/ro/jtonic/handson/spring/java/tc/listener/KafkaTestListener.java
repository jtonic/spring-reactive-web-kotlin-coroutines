package ro.jtonic.handson.spring.java.tc.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ro.jtonic.handson.spring.java.tc.model.TestMessage;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Component
public class KafkaTestListener {

    private static final Logger log = LoggerFactory.getLogger(KafkaTestListener.class);

    private CountDownLatch latch = new CountDownLatch(1);

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

    public CountDownLatch getLatch() {
        return this.latch;
    }

    public TestMessage getReceivedMessage() {
        return this.receivedMessage;
    }
}
