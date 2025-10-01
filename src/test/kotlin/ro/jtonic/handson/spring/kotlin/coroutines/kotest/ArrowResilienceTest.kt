package ro.jtonic.handson.spring.kotlin.coroutines.kotest

import arrow.core.Either
import arrow.core.left
import arrow.resilience.Schedule
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class ArrowResilienceTest : FreeSpec({

    fun CoroutineScope.after1(d: Duration, block: () -> Unit) {
        launch {
            delay(d)
            block()
        }
    }

    fun formattedNow() = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    data class AppError(val description: String, val throwable: Throwable)

    val rdm = Random()

    suspend fun httpCall(): Either<AppError, String> {
        // two initial failures
        repeat(2) {
            delay(10.milliseconds)
            println("Throwing RuntimeException. ${formattedNow()}")
            RuntimeException("Boom!!!").let {
                AppError(it.message!!, it).left()
            }
        }

        // then random failures
        return Either.Companion.catchOrThrow<RuntimeException, String> {
            val res: Int = rdm.nextInt(9)
            if (res % 2 == 0) {
                delay(10.milliseconds)
                println("Throwing RuntimeException. ${formattedNow()}")
                throw RuntimeException("Boom!!!")
            } else "result = $res"
        }.mapLeft { AppError(it.message!!, it) }
    }

    "retry 1" {

        var counter = 1

        Schedule.Companion.recurs<Unit>(9).repeat {
            counter++
            httpCall()
        }

        counter shouldBe 11
    }

    "retry 2" {
        var counter = 0

        val keepLeft = (Schedule.Companion.identity<Int>() zipLeft Schedule.Companion.recurs(3)).repeat {
            counter++
        }
        val keepRight = (Schedule.Companion.recurs<Int>(3) zipRight Schedule.Companion.identity()).repeat {
            counter++
        }

        counter shouldBe 8
        keepLeft shouldBe 3
        keepRight shouldBe 7
    }

    "retry 3" {
        var counter = 0

        val keep = Schedule.Companion.recurs<Int>(3).repeat {
            counter++
        }
        counter shouldBe 4
        keep shouldBe 3
    }

    "retry 4" {
        var result = ""

        Schedule.Companion.doWhile<String> { input, _ -> input.length <= 5 }.repeat {
            result += "a"
            result
        }

        result shouldBe "aaaaaa"
    }

    "retry 5" {
        val res =
            Schedule.Companion
                .doUntil<Either<AppError, String>> { input, _ -> input.isRight() }
                .repeat { httpCall() }
        res.shouldBeRight().also {
            println(it)
            it shouldStartWith "result = "
        }
    }

    "retry 6" {
        val right: Schedule<String, List<String>> = Schedule.Companion
            .exponential<String>(10.milliseconds)
            .doWhile { _, duration -> duration < 60.seconds }
            .andThen(Schedule.Companion.spaced<String>(60.seconds) and Schedule.Companion.recurs(100)).jittered()
            .zipRight(Schedule.Companion.identity<String>().collect())
    }
})