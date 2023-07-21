@file:OptIn(ExperimentalCoroutinesApi::class)

package ro.jtonic.handson.spring.kotlin.coroutines

import arrow.core.Either
import arrow.core.left
import arrow.fx.resilience.Schedule
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.system.measureTimeMillis
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class ArrowResilienceTest : FreeSpec({

    fun CoroutineScope.after1(d: Duration, block: () -> Unit) {
        launch {
            delay(d)
            block()
        }
    }

    fun formattedNow() = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    fun formattedNumber(value: Long) = NumberFormat.getNumberInstance().format(value)

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
        return Either.catchOrThrow<RuntimeException, String> {
            val res: Int = rdm.nextInt(9)
            if (res % 2 == 0) {
                delay(10.milliseconds)
                println("Throwing RuntimeException. ${formattedNow()}")
                throw RuntimeException("Boom!!!")
            } else "result = $res"
        }.mapLeft { AppError(it.message!!, it) }
    }


    fun failingHHttpCall(): Either<AppError, String> {
        println("Throwing RuntimeException. ${formattedNow()}")
        val exc = RuntimeException("Boom!!!")
        return AppError(exc.message!!, exc).left()
    }





    "retry 1" {

        var counter = 1

        Schedule.recurs<Unit>(9).repeat {
            counter++
            httpCall()
        }

        counter shouldBe 11
    }

    "retry 2" {
        var counter = 0

        val keepLeft = (Schedule.identity<Int>() zipLeft Schedule.recurs(3)).repeat {
            counter++
        }
        val keepRight = (Schedule.recurs<Int>(3) zipRight Schedule.identity()).repeat {
            counter++
        }

        counter shouldBe 8
        keepLeft shouldBe 3
        keepRight shouldBe 7
    }

    "retry 3" {
        var counter = 0

        val keep = Schedule.recurs<Int>(3).repeat {
            counter++
        }
        counter shouldBe 4
        keep shouldBe 3
    }

    "retry 4" {
        var result = ""

        Schedule.doWhile<String> { input, _ -> input.length <= 5 }.repeat {
            result += "a"
            result
        }

        result shouldBe "aaaaaa"
    }

    "retry 5" {
        val res =
            Schedule
                .doUntil<Either<AppError, String>> { input, _ -> input.isRight() }
                .repeat { httpCall() }
        res.shouldBeRight().also {
            println(it)
            it shouldStartWith "result = "
        }
    }


    "retry with http calls" - {

        repeat(2) { virtualTimeCase ->

            repeat(2) { httpCase ->

                "retry 6: $virtualTimeCase : $httpCase".config(timeout = 2.minutes) {

                    suspend fun innerTest() {
                        println("=".repeat(80))
                        println(testCase.name.testName)
                        measureTimeMillis {
                            Schedule
                                .exponential<Either<AppError, String>>(100.milliseconds, 2.0)
                                .doWhile { _, duration ->
                                    println("Duration: ${formattedNumber(duration.inWholeMilliseconds)}")
                                    duration < 1.minutes
                                }
                                .doUntil { input, output -> input.isRight() }
                                .repeat {
                                    if (httpCase == 0) {
                                        failingHHttpCall()
                                    } else httpCall()
                                }
                        }.also {
                            println("Total Duration ${formattedNumber(it)}")
                        }
                    }
                    if (virtualTimeCase == 0) innerTest() else runTest { innerTest() }
                }
            }
        }
    }



    "retry 7" {
        val right: Schedule<String, List<String>> = Schedule
            .exponential<String>(10.milliseconds)
            .doWhile { _, duration -> duration < 60.seconds }
            .andThen(Schedule.spaced<String>(60.seconds) and Schedule.recurs(100)).jittered()
            .zipRight(Schedule.identity<String>().collect())
    }
})

