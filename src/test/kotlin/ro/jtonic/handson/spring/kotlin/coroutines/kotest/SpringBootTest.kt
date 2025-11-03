package ro.jtonic.handson.spring.kotlin.coroutines

import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.FreeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldNotBe
import io.opentelemetry.instrumentation.spring.autoconfigure.internal.instrumentation.kafka.KafkaInstrumentationAutoConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.junit.jupiter.Testcontainers
import ro.jtonic.handson.spring.java.JavaMainApp
import ro.jtonic.handson.spring.java.tc.TestContainersConfig

@KtSpringIntegrationTest
@ApplyExtension(extensions = [SpringExtension::class])
class SpringBootTest(val appCtx: ApplicationContext) : FreeSpec({

    "test spring context loads" {
        appCtx shouldNotBe null
    }
})

@Testcontainers
@SpringBootTest(classes = [JavaMainApp::class])
@Import(TestContainersConfig::class)
@ActiveProfiles(profiles = ["default", "tst"])
@EnableAutoConfiguration(exclude = [KafkaInstrumentationAutoConfiguration::class])
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class KtSpringIntegrationTest
