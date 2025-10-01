package ro.jtonic.handson.spring

import arrow.core.andThen
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import ro.jtonic.handson.spring.Fp.Operation

object Fp {
    fun interface Operation<in I, out O> {
        operator fun invoke(i: I): O
        fun asFn(): (I) -> O = { i: I -> this(i) }
        infix fun <R> andThen(f: Operation<O, R>): Operation<I, R> = Operation<I, R> { i -> f(this.invoke(i)) }
    }
    fun <I, O> ((I) -> O).asOp(): Operation<I, O> = Operation<I, O> { i -> this(i) }
}

class UseCases2 : FreeSpec({

    val op1: Operation<Unit, String> = Operation<Unit, String> { "Performing op1..." }
    val op2 = Operation<String, Int> { it.length }

    "test 1" {
        (op1 andThen op2)(Unit) shouldBe "Performing op1...".length

        val op3: (Unit) -> String = { "Hello" }
        val op4: (String) -> Int = { it.length }

        (op3 andThen op4)(Unit) shouldBe 5
    }

    "test 2" {
        val op1: Operation<Unit, String> = Operation<Unit, String> { "Performing op1..." }
        val op2 = Operation<String, Int> { it.length }
        (op1 andThen op2)(Unit) shouldBe "Performing op1...".length
    }

    "Test 3" {
        val op1 = Operation<Unit, String> { "Hello" }.asFn()
        val op2 = Operation<String, Int> { it.length }.asFn()
        (op1 andThen op2)(Unit) shouldBe "Hello".length
    }
})