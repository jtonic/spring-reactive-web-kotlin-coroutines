package ro.jtonic.handson.spring

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

fun interface Walkable {
    fun walk(): String
}

fun interface Breathable {
    fun breath(): String
}

fun <T> live(t: T): String where T: Breathable, T: Walkable =
    "${t.breath()} - ${t.walk()}"

class Cat : Breathable, Walkable {
    override fun breath(): String = "breathing..."
    override fun walk(): String = "walking..."
}

class Dog(w: Walkable, b: Breathable): Walkable by w, Breathable by b

class UseCasesTest : FreeSpec({
    "test composition 1" {
        val cat = Cat()
        live(cat) shouldBe "breathing... - walking..."
    }
    "test composition 2" {
        val w1 = Walkable { "Walking steadily..." }
        val b1 = Breathable { "Breathing smoothly..." }
        live(Dog(w1, b1)) shouldBe "Breathing smoothly... - Walking steadily..."
    }
})