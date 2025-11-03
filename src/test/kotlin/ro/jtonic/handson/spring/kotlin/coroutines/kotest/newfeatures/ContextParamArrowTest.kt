package ro.jtonic.handson.spring.kotlin.coroutines.kotest.newfeatures

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class ContextParamArrowTest : FreeSpec({

    "arrow effects by using either" {
        either {
            crazyAdd(1, 2).bind()
        } shouldBe Either.Right(3)
    }
})

data class NotFoundError(override val message: String) : RuntimeException(message)

fun crazyAdd(a: Int, b: Int) = either {
    ensure(a != b) { NotFoundError("Boom!!!") }
    a + b
}
