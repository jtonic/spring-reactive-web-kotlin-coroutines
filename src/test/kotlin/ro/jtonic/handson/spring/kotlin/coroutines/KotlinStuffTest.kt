package ro.jtonic.handson.spring.kotlin.coroutines

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class KotlinStuffTest {

    @Test
    fun test() {
        val err1 = FatalError
        val err2 = TechnicalError("An error has occurred when calling external service")

        fun handleError(err: ApplicationError) = when (err) {
            is FatalError -> err.toString()
            is TechnicalError -> "${err.errorCode} > ${err.message}"
        }
        handleError(err1).also {
            assertTrue(it == "FatalError")
        }
        handleError(err2).also {
            println(it)
        }
    }
}

sealed class ApplicationError(val errorCode: String, open val message: String)

data object FatalError : ApplicationError("0", "Fatal Error")

class TechnicalError(override val message: String) : ApplicationError(errorCode = "1", message = message)
