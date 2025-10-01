package ro.jtonic.handson.spring.kotlin.coroutines.kotest

import io.kotest.core.annotation.Ignored
import io.kotest.core.spec.style.FunSpec

@Ignored("Not sure why on this project there is a strange error, like dependencies misconfiguration")
class TestDispatcherTest : FunSpec() {
    init {
        test("foo").config(coroutineTestScope = true) {
            // this test will run with a test dispatcher
        }
    }
}