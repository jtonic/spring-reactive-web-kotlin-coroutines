package ro.jtonic.handson.spring.kotlin.coroutines

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import ro.jtonic.handson.spring.kotlin.coroutines.Genre.FEMALE
import ro.jtonic.handson.spring.kotlin.coroutines.Genre.MALE

enum class Genre {
    MALE, FEMALE
}
class Kt20EnumTest : FreeSpec ({

    "simplified enum when in kotlin 2+" {
        val tonyG = MALE
        when (tonyG) {
            FEMALE -> "female"
            MALE -> "male"
        } shouldBe "male"
    }
})