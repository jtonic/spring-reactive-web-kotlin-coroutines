package ro.jtonic.handson.spring.java.tc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import ro.jtonic.handson.spring.java.JavaSpringIntegrationTest;
import ro.jtonic.handson.spring.java.tc.listener.KafkaTestListener;
import ro.jtonic.handson.spring.java.tc.model.TestMessage;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JavaSpringIntegrationTest
public class KafkaTcTest {

    @Autowired
    private KafkaTemplate<String, TestMessage> kafkaTemplate;

    @Autowired
    private KafkaTestListener listener;

    @Value("${test.topic}")
    private String topic;

    @BeforeEach
    void setUp() {
        listener.resetLatch();
    }

    @Test
    public void testKafkaListenerReceivesMessage() throws Exception {
        // Arrange
        String id = UUID.randomUUID().toString();
        String content = "Hello, Kafka!";
        TestMessage message = new TestMessage(id, content);
        
        // Act: Send message using KafkaTemplate
        kafkaTemplate.send(topic, id, message).get(10, TimeUnit.SECONDS);
        
        // Assert: Wait for the listener to process the message
        boolean messageReceived = listener.awaitMessage(10, TimeUnit.SECONDS);
        
        assertTrue(messageReceived, "Message not received within the timeout");
        assertEquals(id, listener.getReceivedMessage().id());
        assertEquals(content, listener.getReceivedMessage().content());
    }
}
