package ro.jtonic.handson.spring.kotlin.newfeatures

import arrow.core.raise.Raise
import arrow.core.raise.either
import arrow.core.raise.ensure
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import ro.jtonic.handson.spring.kotlin.newfeatures.AppError.UserNotFoundError

class NewFeaturesSpec : FreeSpec({

    "context parameters" {

        context(userService: UserService, roleCheckService: RoleCheckService, raise: Raise<AppError>)
        fun doFindNameByUserId(id: Int, role: String): User =
//          ensure(id > 0) { "User ID must be positive" }
            if (roleCheckService.check(role)) {
                userService.findNameById(id)
            } else {
                raise.raise(UserNotFoundError)
            }

        val userService: UserService = object : UserService {
            override fun findNameById(id: Int): User =
                User("User by it '$id'")
        }

        val roleCheck: RoleCheckService = object : RoleCheckService {
            override fun check(role: String): Boolean = role == "admin"
        }

        val userId = 1;
        val anEither = either {
            ensure(1 > 0) { "User ID must be positive" }
            context(userService, roleCheck) {
                doFindNameByUserId(id = userId, role = "admin")
            }
        }
        anEither.shouldBeRight(User("User by it '$userId'"))
    }
})

interface UserService {
    fun findNameById(id: Int): User
}

interface RoleCheckService {
    fun check(role: String): Boolean
}

data class User(val name: String)

sealed class AppError(msg: String) {
    data object UserNotFoundError : AppError("User not found") {}
}
