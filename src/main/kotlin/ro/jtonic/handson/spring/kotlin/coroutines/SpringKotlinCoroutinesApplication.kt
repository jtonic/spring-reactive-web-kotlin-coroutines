package ro.jtonic.handson.spring.kotlin.coroutines

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(BBonbProps::class)
class SpringKotlinCoroutinesApplication

fun main(args: Array<String>) {
	runApplication<SpringKotlinCoroutinesApplication>(*args)
}
