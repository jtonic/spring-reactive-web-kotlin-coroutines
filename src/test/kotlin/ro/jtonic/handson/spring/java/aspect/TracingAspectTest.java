package ro.jtonic.handson.spring.java.aspect;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("tst")
public class TracingAspectTest {

    @Autowired
    private TestService testService;

    private ListAppender<ILoggingEvent> listAppender;
    private Logger tracingLogger;

    @BeforeEach
    public void setup() {
        tracingLogger = (Logger) LoggerFactory.getLogger(TracingAspect.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        tracingLogger.addAppender(listAppender);

        listAppender.list.clear();
    }

    @AfterEach
    public void teardown() {
        tracingLogger.detachAppender(listAppender);
    }

    @Test
    public void testTracingWithDefaultSettings() {
        String result = testService.methodWithDefaultTracing("test input", "test-trace-id-123");

        assertEquals("Processed: test input with trace ID: test-trace-id-123", result);

        boolean hasEntryLog = listAppender.list.stream()
                .anyMatch(event -> event.getFormattedMessage().contains("Entering method TestService.methodWithDefaultTracing"));

        boolean hasExitLog = listAppender.list.stream()
                .anyMatch(event -> event.getFormattedMessage().contains("Exiting method TestService.methodWithDefaultTracing"));

        assertTrue(hasEntryLog, "There should be a log entry for method entry");
        assertTrue(hasExitLog, "There should be a log entry for method exit");
    }

    @Test
    public void testTracingWithDebugLevel() {
        Level originalLevel = tracingLogger.getLevel();
        tracingLogger.setLevel(Level.DEBUG);

        try {
            int result = testService.methodWithDebugTracing(5, "debug-trace-id-456");

            assertEquals(10, result);

            boolean hasDebugLog = listAppender.list.stream()
                    .anyMatch(event -> event.getFormattedMessage().contains("TestService.methodWithDebugTracing")
                                       && event.getLevel() == Level.DEBUG);

            assertTrue(hasDebugLog, "There should be a log entry at DEBUG level");
        } finally {
            tracingLogger.setLevel(originalLevel);
        }
    }

    @Test
    public void testDisabledTracing() {
        Level originalLevel = tracingLogger.getLevel();
        tracingLogger.setLevel(Level.DEBUG);

        try {
            testService.methodWithDisabledTracing();

            boolean hasDisabledLog = listAppender.list.stream()
                    .anyMatch(event -> event.getFormattedMessage().contains("Tracing disabled for TestService.methodWithDisabledTracing"));

            boolean hasEntryLog = listAppender.list.stream()
                    .anyMatch(event -> event.getFormattedMessage().contains("Entering method TestService.methodWithDisabledTracing"));

            assertTrue(hasDisabledLog, "There should be a log indicating that tracing is disabled");
            assertFalse(hasEntryLog, "There should not be a log entry for method entry");
        } finally {
            tracingLogger.setLevel(originalLevel);
        }
    }

    @Test
    public void testTracingWithException() {
        try {
            testService.methodWithException("");
            fail("An exception should have been thrown");
        } catch (IllegalArgumentException e) {
            boolean hasExceptionLog = listAppender.list.stream()
                    .anyMatch(event -> event.getFormattedMessage().contains("Exception in method TestService.methodWithException"));

            assertTrue(hasExceptionLog, "There should be a log entry for the exception");
        }
    }

    @Test
    public void testMethodWithoutTracing() {
        listAppender.list.clear();

        testService.methodWithoutTracing("test");

        boolean hasMethodLog = listAppender.list.stream()
                .anyMatch(event -> event.getFormattedMessage().contains("TestService.methodWithoutTracing"));

        assertFalse(hasMethodLog, "There should not be any logs for a non-annotated method");
    }

    @Test
    public void testKafkaListenerWithTraceHeader() {
        Level originalLevel = tracingLogger.getLevel();
        tracingLogger.setLevel(Level.DEBUG);

        try {
            listAppender.list.clear();

            String traceId = "kafka-trace-id-abc123";
            testService.methodWithDefaultTracing("kafka message", traceId);

            boolean hasTraceIdLog = listAppender.list.stream()
                    .anyMatch(event -> event.getFormattedMessage().contains("In KafkaListener with malformed uber-trace-id: " + traceId));

            assertTrue(hasTraceIdLog, "There should be a log entry with the trace ID value");
        } finally {
            tracingLogger.setLevel(originalLevel);
        }
    }

    @Test
    public void testKafkaListenerWithDebugTraceHeader() {
        Level originalLevel = tracingLogger.getLevel();
        tracingLogger.setLevel(Level.DEBUG);

        try {
            listAppender.list.clear();

            String traceId = "kafka-debug-trace-id-xyz789";
            testService.methodWithDebugTracing(10, traceId);

            boolean hasTraceIdLog = listAppender.list.stream()
                    .anyMatch(event -> event.getFormattedMessage().contains("In KafkaListener with malformed uber-trace-id: " + traceId));

            boolean hasDebugLog = listAppender.list.stream()
                    .anyMatch(event -> event.getFormattedMessage().contains("Entering method TestService.methodWithDebugTracing")
                                       && event.getLevel() == Level.DEBUG);

            assertTrue(hasTraceIdLog, "There should be a log entry with the trace ID value");
            assertTrue(hasDebugLog, "There should be a DEBUG level log for method entry");
        } finally {
            tracingLogger.setLevel(originalLevel);
        }
    }

    @Test
    public void testKafkaListenerWithNullTraceId() {
        Level originalLevel = tracingLogger.getLevel();
        tracingLogger.setLevel(Level.DEBUG);

        try {
            listAppender.list.clear();

            testService.methodWithDefaultTracingNullable("test input", null);

            boolean hasTraceIdProcessingLog = listAppender.list.stream()
                    .anyMatch(event -> event.getFormattedMessage().contains("In KafkaListener processing message with traceId"));

            assertFalse(hasTraceIdProcessingLog, "There should not be any logs about processing trace ID when it's null");

            boolean hasEntryLog = listAppender.list.stream()
                    .anyMatch(event -> event.getFormattedMessage().contains("Entering method TestService.methodWithDefaultTracingNullable"));

            assertTrue(hasEntryLog, "There should still be normal method entry logs");

        } finally {
            tracingLogger.setLevel(originalLevel);
        }
    }

    @Test
    public void testKafkaListenerWithFormattedTraceId() {
        Level originalLevel = tracingLogger.getLevel();
        tracingLogger.setLevel(Level.DEBUG);

        try {
            listAppender.list.clear();

            String formattedTraceId = "abcd1234:wxyz:5678:1";
            testService.methodWithDefaultTracing("test input", formattedTraceId);

            boolean hasTraceComponentsLog = listAppender.list.stream()
                    .anyMatch(event ->
                            event.getFormattedMessage().contains("In KafkaListener processing message with valid traceId: abcd1234, spanId: wxyz, parentSpanId: 5678, ext: 1"));

            assertTrue(hasTraceComponentsLog, "The trace ID should be parsed into its components and validated as correct");

        } finally {
            tracingLogger.setLevel(originalLevel);
        }
    }

    @Test
    public void testKafkaListenerWithMalformedTraceId() {
        Level originalLevel = tracingLogger.getLevel();
        tracingLogger.setLevel(Level.DEBUG);

        try {
            listAppender.list.clear();

            String malformedTraceId = "abc123:def456";
            testService.methodWithDefaultTracing("test input", malformedTraceId);

            boolean hasMalformedTraceIdLog = listAppender.list.stream()
                    .anyMatch(event ->
                            event.getFormattedMessage().contains("In KafkaListener with malformed uber-trace-id: abc123:def456"));

            assertTrue(hasMalformedTraceIdLog, "Malformed trace IDs should be detected and logged");

        } finally {
            tracingLogger.setLevel(originalLevel);
        }
    }

    @Test
    public void testKafkaListenerWithInvalidTraceComponents() {
        Level originalLevel = tracingLogger.getLevel();
        tracingLogger.setLevel(Level.DEBUG);

        try {
            listAppender.list.clear();

            String invalidTraceId = "12345:abcde:xyz:1";
            testService.methodWithDefaultTracing("test input", invalidTraceId);

            boolean hasInvalidComponentsLog = listAppender.list.stream()
                    .anyMatch(event ->
                            event.getFormattedMessage().contains("In KafkaListener processing message with invalid trace components - traceId valid: false, spanId valid: false, parentSpanId valid: false"));

            assertTrue(hasInvalidComponentsLog, "Invalid trace components should be detected and logged");

        } finally {
            tracingLogger.setLevel(originalLevel);
        }
    }
}
