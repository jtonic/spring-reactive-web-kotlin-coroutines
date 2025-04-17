package ro.jtonic.handson.spring.java.aspect

import org.springframework.stereotype.Service
import ro.jtonic.handson.spring.java.aspect.annotation.Header
import ro.jtonic.handson.spring.java.aspect.annotation.KafkaListener

@Service
class TestService {

    @EnableTracing
    @KafkaListener(topics = ["default-topic"])
    fun methodWithDefaultTracing(input: String, @Header(name = "uber-trace-id") traceId: String): String {
        return "Processed: $input with trace ID: $traceId"
    }

    @EnableTracing
    @KafkaListener(topics = ["nullable-topic"])
    fun methodWithDefaultTracingNullable(input: String, @Header(name = "uber-trace-id") traceId: String?): String {
        return "Processed: $input with trace ID: ${traceId ?: "none"}"
    }

    @EnableTracing(level = "DEBUG")
    @KafkaListener(topics = ["debug-topic"])
    fun methodWithDebugTracing(value: Int, @Header(name = "uber-trace-id") traceId: String): Int {
        return value * 2
    }

    @EnableTracing(enabled = false)
    fun methodWithDisabledTracing() {
    }

    @EnableTracing
    fun methodWithException(input: String): String {
        if (input.isEmpty()) {
            throw IllegalArgumentException("Input cannot be empty")
        }
        return input.uppercase()
    }

    fun methodWithoutTracing(input: String): String {
        return "Not traced: $input"
    }
}
