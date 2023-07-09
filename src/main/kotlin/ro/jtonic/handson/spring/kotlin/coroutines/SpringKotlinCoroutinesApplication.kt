package ro.jtonic.handson.spring.kotlin.coroutines

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringKotlinCoroutinesApplication

fun main(args: Array<String>) {
	runApplication<SpringKotlinCoroutinesApplication>(*args)
}
