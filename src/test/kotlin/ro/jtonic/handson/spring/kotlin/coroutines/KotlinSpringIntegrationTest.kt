package ro.jtonic.handson.spring.kotlin.coroutines

import io.opentelemetry.instrumentation.spring.autoconfigure.internal.instrumentation.kafka.KafkaInstrumentationAutoConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import org.testcontainers.junit.jupiter.Testcontainers
import ro.jtonic.handson.spring.java.tc.TestContainersConfig


@Testcontainers
@SpringBootTest(
    classes = [SpringKotlinCoroutinesApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Import(TestContainersConfig::class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ActiveProfiles(profiles = ["default", "tst"])
@EnableAutoConfiguration(exclude = [KafkaInstrumentationAutoConfiguration::class])
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class KotlinSpringIntegrationTest