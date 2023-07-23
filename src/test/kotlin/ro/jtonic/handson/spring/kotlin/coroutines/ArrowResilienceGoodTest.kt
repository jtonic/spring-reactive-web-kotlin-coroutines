@file:OptIn(ExperimentalCoroutinesApi::class)

package ro.jtonic.handson.spring.kotlin.coroutines

import arrow.core.Either
import arrow.core.left
import arrow.resilience.Schedule
import io.kotest.core.spec.style.FreeSpec
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.system.measureTimeMillis
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

class ArrowResilienceGoodTest : FreeSpec({

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
})

