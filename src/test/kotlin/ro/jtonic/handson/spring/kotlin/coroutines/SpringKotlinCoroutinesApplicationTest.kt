package ro.jtonic.handson.spring.kotlin.coroutines

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SpringKotlinCoroutinesApplicationTest(
	@Autowired private val runFlow: UseCase<Input, Unit>,
) {

	@Test
	fun contextLoads() {
		runFlow(Input(10))
	}
}
