package ro.jtonic.handson.spring.java.aspect

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import ro.jtonic.handson.spring.java.JavaSpringIntegrationTest
import java.time.Duration
import java.time.Instant

@JavaSpringIntegrationTest
@Disabled("Put it back when we have open telemetry back")
class EnableTracingPerformanceTest {

    @Autowired
    private lateinit var testService: TestService

    private companion object {
        const val ITERATIONS = 1000
    }

    @Test
    @Disabled
    fun testTracingPerformanceOverhead() {
        val startTraced = Instant.now()
        (1..ITERATIONS).forEach { _ ->
            testService.methodWithDefaultTracing("test", "trace123:span:prnt:1")
        }
        val tracedDuration = Duration.between(startTraced, Instant.now())
    
        val startUntraced = Instant.now()
        (1..ITERATIONS).forEach { _ ->
            testService.methodWithoutTracing("test")
        }
        val untracedDuration = Duration.between(startUntraced, Instant.now())

        println("Execution time with tracing: ${tracedDuration.toMillis()}ms")
        println("Execution time without tracing: ${untracedDuration.toMillis()}ms")
        println("Overhead per operation: ${(tracedDuration.toMillis() - untracedDuration.toMillis()).toFloat() / ITERATIONS}ms")

        assertTrue(
            tracedDuration.compareTo(untracedDuration.multipliedBy(50)) < 0,
            "Tracing overhead should be reasonable"
        )
    }
}
