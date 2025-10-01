package ro.jtonic.handson.spring.kotlin.coroutines

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldStartWith

class KotlinStuffTest : FreeSpec({

    "test typed errors handling" {
        val err1 = FatalError
        val err2 = TechnicalError("An error has occurred when calling external service")

        fun handleError(err: ApplicationError) = when (err) {
            is FatalError -> err.toString()
            is TechnicalError -> "${err.errorCode} > ${err.message}"
        }
        handleError(err1) shouldBe "FatalError"
        handleError(err2) shouldContain "An error has occurred"
    }

    "test handling errors with new pattern match" {
        fun handleError(err: ApplicationError) = when (err) {
            is FatalError -> err.toString()
            is TechnicalError if err.message.startsWith("An error") -> "${err.errorCode} > ${err.message}"
            else -> "Unknown message"
        }

        val err1 = FatalError
        val err2 = TechnicalError("An HTTP call error occurred.")

        handleError(err2) shouldBe "Unknown message"
        handleError(TechnicalError("An error")) shouldStartWith "1 > "
    }
})

sealed class ApplicationError(val errorCode: String, open val message: String)

data object FatalError : ApplicationError("0", "Fatal Error")

class TechnicalError(override val message: String) : ApplicationError(errorCode = "1", message = message)
