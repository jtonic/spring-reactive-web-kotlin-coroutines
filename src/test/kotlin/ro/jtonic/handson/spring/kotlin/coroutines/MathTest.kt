package ro.jtonic.handson.spring.kotlin.coroutines

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import java.math.BigInteger

class MathTest : FreeSpec({
    listOf(0, 1_000, 49_000_000).forEach { number ->
        "$number is greater or equals that zero operations" {
            (number.toBigInteger() >= BigInteger.ZERO) shouldBe true
        }
    }
    listOf(1_000, 49_000_000).forEach { number ->
        "$number should be lesser that 50.000.000" {
            (number.toBigInteger() >= BigInteger.ZERO) shouldBe true
        }
    }

    listOf(-1, 51_000_000).forEach { number ->
        "$number should is not a valid input data" {
            (number.toBigInteger() >= BigInteger.ZERO && number.toBigInteger() < 50_000_000.toBigInteger()) shouldBe false
        }
    }
})