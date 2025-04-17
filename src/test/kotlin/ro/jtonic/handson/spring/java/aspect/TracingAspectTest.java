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
        // Configure appender to capture logs
        tracingLogger = (Logger) LoggerFactory.getLogger(TracingAspect.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        tracingLogger.addAppender(listAppender);

        // Clear logs before each test
        listAppender.list.clear();
    }

    @AfterEach
    public void teardown() {
        tracingLogger.detachAppender(listAppender);
    }

    @Test
    public void testTracingWithDefaultSettings() {
        // Execute method with default tracing
        String result = testService.methodWithDefaultTracing("test input", "test-trace-id-123");

        assertEquals("Processed: test input with trace ID: test-trace-id-123", result);

        // Verify that logs contain method entry and exit
        boolean hasEntryLog = listAppender.list.stream()
                .anyMatch(event -> event.getFormattedMessage().contains("Entering method TestService.methodWithDefaultTracing"));

        boolean hasExitLog = listAppender.list.stream()
                .anyMatch(event -> event.getFormattedMessage().contains("Exiting method TestService.methodWithDefaultTracing"));

        assertTrue(hasEntryLog, "There should be a log entry for method entry");
        assertTrue(hasExitLog, "There should be a log entry for method exit");
    }

    @Test
    public void testTracingWithDebugLevel() {
        // Temporarily set log level to DEBUG for this test
        Level originalLevel = tracingLogger.getLevel();
        tracingLogger.setLevel(Level.DEBUG);

        try {
            int result = testService.methodWithDebugTracing(5, "debug-trace-id-456");

            assertEquals(10, result);

            // Verify that logs are at DEBUG level
            boolean hasDebugLog = listAppender.list.stream()
                    .anyMatch(event -> event.getFormattedMessage().contains("TestService.methodWithDebugTracing")
                                       && event.getLevel() == Level.DEBUG);

            assertTrue(hasDebugLog, "There should be a log entry at DEBUG level");
        } finally {
            // Restore original log level
            tracingLogger.setLevel(originalLevel);
        }
    }

    @Test
    public void testDisabledTracing() {
        // Temporarily set log level to DEBUG to see the disabled message
        Level originalLevel = tracingLogger.getLevel();
        tracingLogger.setLevel(Level.DEBUG);

        try {
            testService.methodWithDisabledTracing();

            // Verify that there is only a log indicating that tracing is disabled
            boolean hasDisabledLog = listAppender.list.stream()
                    .anyMatch(event -> event.getFormattedMessage().contains("Tracing disabled for TestService.methodWithDisabledTracing"));

            // Verify that there are no entry/exit logs
            boolean hasEntryLog = listAppender.list.stream()
                    .anyMatch(event -> event.getFormattedMessage().contains("Entering method TestService.methodWithDisabledTracing"));

            assertTrue(hasDisabledLog, "There should be a log indicating that tracing is disabled");
            assertFalse(hasEntryLog, "There should not be a log entry for method entry");
        } finally {
            // Restore original log level
            tracingLogger.setLevel(originalLevel);
        }
    }

    @Test
    public void testTracingWithException() {
        try {
            testService.methodWithException("");
            fail("An exception should have been thrown");
        } catch (IllegalArgumentException e) {
            // Verify that there is a log for the exception
            boolean hasExceptionLog = listAppender.list.stream()
                    .anyMatch(event -> event.getFormattedMessage().contains("Exception in method TestService.methodWithException"));

            assertTrue(hasExceptionLog, "There should be a log entry for the exception");
        }
    }

    @Test
    public void testMethodWithoutTracing() {
        // Clear logs before the test
        listAppender.list.clear();

        testService.methodWithoutTracing("test");

        // Verify that there are no logs related to this method
        boolean hasMethodLog = listAppender.list.stream()
                .anyMatch(event -> event.getFormattedMessage().contains("TestService.methodWithoutTracing"));

        assertFalse(hasMethodLog, "There should not be any logs for a non-annotated method");
    }

    @Test
    public void testKafkaListenerWithTraceHeader() {
        // Temporarily set log level to DEBUG for this test
        Level originalLevel = tracingLogger.getLevel();
        tracingLogger.setLevel(Level.DEBUG);

        try {
            // Clear logs before the test
            listAppender.list.clear();

            String traceId = "kafka-trace-id-abc123";
            testService.methodWithDefaultTracing("kafka message", traceId);

            // Verify that there is a log containing the trace ID
            boolean hasTraceIdLog = listAppender.list.stream()
                    .anyMatch(event -> event.getFormattedMessage().contains("In KafkaListener with malformed uber-trace-id: " + traceId));

            assertTrue(hasTraceIdLog, "There should be a log entry with the trace ID value");
        } finally {
            // Restore original log level
            tracingLogger.setLevel(originalLevel);
        }
    }

    @Test
    public void testKafkaListenerWithDebugTraceHeader() {
        // Temporarily set log level to DEBUG for this test
        Level originalLevel = tracingLogger.getLevel();
        tracingLogger.setLevel(Level.DEBUG);

        try {
            // Clear logs before the test
            listAppender.list.clear();

            String traceId = "kafka-debug-trace-id-xyz789";
            testService.methodWithDebugTracing(10, traceId);

            // Verify that there is a log containing the trace ID
            boolean hasTraceIdLog = listAppender.list.stream()
                    .anyMatch(event -> event.getFormattedMessage().contains("In KafkaListener with malformed uber-trace-id: " + traceId));

            // Also verify that the method was executed with DEBUG level logging
            boolean hasDebugLog = listAppender.list.stream()
                    .anyMatch(event -> event.getFormattedMessage().contains("Entering method TestService.methodWithDebugTracing")
                                       && event.getLevel() == Level.DEBUG);

            assertTrue(hasTraceIdLog, "There should be a log entry with the trace ID value");
            assertTrue(hasDebugLog, "There should be a DEBUG level log for method entry");
        } finally {
            // Restore original log level
            tracingLogger.setLevel(originalLevel);
        }
    }

    @Test
    public void testKafkaListenerWithNullTraceId() {
        // Temporarily set log level to DEBUG for this test
        Level originalLevel = tracingLogger.getLevel();
        tracingLogger.setLevel(Level.DEBUG);

        try {
            // Clear logs before the test
            listAppender.list.clear();

            // Execute the method with a null trace ID
            testService.methodWithDefaultTracingNullable("test input", null);

            // Verify the method was executed without exceptions
            // There should be no logs about processing the uber-trace-id since it's null
            boolean hasTraceIdProcessingLog = listAppender.list.stream()
                    .anyMatch(event -> event.getFormattedMessage().contains("In KafkaListener processing message with traceId"));

            assertFalse(hasTraceIdProcessingLog, "There should not be any logs about processing trace ID when it's null");

            // But we should still have the normal entry/exit method tracing
            boolean hasEntryLog = listAppender.list.stream()
                    .anyMatch(event -> event.getFormattedMessage().contains("Entering method TestService.methodWithDefaultTracingNullable"));

            assertTrue(hasEntryLog, "There should still be normal method entry logs");

        } finally {
            // Restore original log level
            tracingLogger.setLevel(originalLevel);
        }
    }

    @Test
    public void testKafkaListenerWithFormattedTraceId() {
        // Temporarily set log level to DEBUG for this test
        Level originalLevel = tracingLogger.getLevel();
        tracingLogger.setLevel(Level.DEBUG);

        try {
            // Clear logs before the test
            listAppender.list.clear();

            // Execute the method with a properly formatted trace ID (contains all 4 parts)
            String formattedTraceId = "abc123:def456:ghi789:1";
            testService.methodWithDefaultTracing("test input", formattedTraceId);

            // Verify that the trace ID was parsed correctly and logged with its components
            boolean hasTraceComponentsLog = listAppender.list.stream()
                    .anyMatch(event ->
                            event.getFormattedMessage().contains("In KafkaListener processing message with traceId: abc123, spanId: def456, parentSpanId: ghi789, ext: 1"));

            assertTrue(hasTraceComponentsLog, "The trace ID should be parsed into its components");

        } finally {
            // Restore original log level
            tracingLogger.setLevel(originalLevel);
        }
    }

    @Test
    public void testKafkaListenerWithMalformedTraceId() {
        // Temporarily set log level to DEBUG for this test
        Level originalLevel = tracingLogger.getLevel();
        tracingLogger.setLevel(Level.DEBUG);

        try {
            // Clear logs before the test
            listAppender.list.clear();

            // Execute the method with a malformed trace ID (missing components)
            String malformedTraceId = "abc123:def456";
            testService.methodWithDefaultTracing("test input", malformedTraceId);

            // Verify that the malformed trace ID was detected and logged
            boolean hasMalformedTraceIdLog = listAppender.list.stream()
                    .anyMatch(event ->
                            event.getFormattedMessage().contains("In KafkaListener with malformed uber-trace-id: abc123:def456"));

            assertTrue(hasMalformedTraceIdLog, "Malformed trace IDs should be detected and logged");

        } finally {
            // Restore original log level
            tracingLogger.setLevel(originalLevel);
        }
    }
}
