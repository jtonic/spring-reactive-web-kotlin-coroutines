package ro.jtonic.handson.spring.kotlin.coroutines.junit

import ro.jtonic.handson.spring.kotlin.coroutines.Input
import ro.jtonic.handson.spring.kotlin.coroutines.KotlinSpringIntegrationTest
import ro.jtonic.handson.spring.kotlin.coroutines.RunFlow
import kotlin.test.Test

@KotlinSpringIntegrationTest
class SpringKotlinCoroutinesApplicationIT(
    private val runFlow: RunFlow
) {
    @Test
    fun contextLoads() {
        runFlow(Input(10))
    }
}