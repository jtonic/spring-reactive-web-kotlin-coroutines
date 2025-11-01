package ro.jtonic.handson.spring.kotlin.coroutines

import io.opentelemetry.instrumentation.spring.autoconfigure.internal.instrumentation.kafka.KafkaInstrumentationAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication(exclude = [KafkaInstrumentationAutoConfiguration::class])
@EnableConfigurationProperties(BBonbProps::class, WebApiProps::class)
@EnableScheduling
class SpringKotlinCoroutinesApplication

fun main(args: Array<String>) {
	runApplication<SpringKotlinCoroutinesApplication>(*args)
}
