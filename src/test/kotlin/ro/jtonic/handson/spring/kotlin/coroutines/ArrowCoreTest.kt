package ro.jtonic.handson.spring.kotlin.coroutines

import arrow.core.curried
import io.kotest.core.spec.style.FreeSpec

object Singleton {
    inline fun <reified A, reified B> concatenate(a: A, b: B): String = "$a$b"
    inline fun <reified A> concat2(a: String, b: String, c: String): String {
        val a = "$a$b ${A::class.java}"
        return a
    }
}

class ArrowCoreTest : FreeSpec({

    "test currying with generics" {
        val f = { a: String, b: String -> Singleton.concatenate(a, b) }
        val a = f.curried()

        val b = a("1")
        val c = b("2")
        println("c = $c")
    }

    "2" {
        val func: (String) -> (String) -> (String) -> String = { a: String, b: String, c: String ->
            Singleton.concat2<Int>(a, b, c)
        }.curried()
        val func1 = func("a")("b")
        val res = func1("c")
        println("res = $res")
    }

})