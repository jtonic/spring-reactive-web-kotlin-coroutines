package ro.jtonic.handson.spring.kotlin.coroutines.kotest

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensureNotNull
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.assertions.arrow.core.shouldNotBeLeft
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import ro.jtonic.handson.spring.kotlin.coroutines.Student

class StudentTest : FreeSpec({

    val student: Student? = Student("Bob", 18, Student.Street("Avenue 12", null))

    "with dummy Option - java 1.7" {
        Student.getStateFromJava7(student) shouldBe "unknown"
    }

    "with dummy Option - java 8" {
        val student = Student("Bob", 18, Student.Street("Avenue 12", null))
        Student.getStateFromJava8(student) shouldBe "unknown"
    }

    "with powerful null safe operator" {
        val state = student?.address?.city?.state?.name ?: "unknown"
        state shouldBe "unknown"
    }

    "with either" - {
        fun getState(student: Student?): Either<String, String> = either {
            ensureNotNull(student) { "No student" }
            ensureNotNull(student.address) { "Missing address" }
            ensureNotNull(student.address.city) { "Missing city" }
            ensureNotNull(student.address.city.state) { "Missing state" }
            ensureNotNull(student.address.city.state.name) { "Missing state name" }
            student.address.city.state.name
        }

        "student missing city" {
            val state = getState(student)
            state shouldBeLeft "Missing city"
            state shouldNotBeLeft "Missing address"
        }

        "full info student" {
            val student2 =
                Student("Bob", 18, Student.Street("Avenue 12", Student.City("Avenue Park", Student.State("New York"))))
            val state = getState(student2)
            state shouldBeRight "New York"
        }
    }
})