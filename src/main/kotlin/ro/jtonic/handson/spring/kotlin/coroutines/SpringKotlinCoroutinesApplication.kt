package ro.jtonic.handson.spring.kotlin.coroutines

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableConfigurationProperties(BBonbProps::class)
@EnableScheduling
class SpringKotlinCoroutinesApplication

fun main(args: Array<String>) {
	runApplication<SpringKotlinCoroutinesApplication>(*args)
}
