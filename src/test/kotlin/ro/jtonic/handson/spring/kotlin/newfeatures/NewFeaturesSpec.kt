package ro.jtonic.handson.spring.kotlin.newfeatures

import arrow.core.raise.Raise
import arrow.core.raise.either
import arrow.core.raise.ensure
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import ro.jtonic.handson.spring.kotlin.newfeatures.AppError.UserNotFoundError

class NewFeaturesSpec : FreeSpec({

    "context parameters" {

        context(r: Raise<AppError>)
        fun checkRole(role: String): User {
            r.ensure(role.isNotEmpty()) { AppError.InvalidRoleError }
            return User("Tony")
        }

        context(userService: UserService, roleCheckService: RoleCheckService, r: Raise<AppError>)
        fun doFindNameByUserId(id: Int, role: String): User =
            if (roleCheckService.check(role)) {
                userService.findNameById(id)
            } else {
                r.raise(UserNotFoundError)
            }

        val userService: UserService = object : UserService {
            override fun findNameById(id: Int): User =
                User("User by it '$id'")
        }

        val roleCheck: RoleCheckService = object : RoleCheckService {
            override fun check(role: String): Boolean = role == "admin"
        }

        val userId = 1;
        val role = "admin"
        val eow = either {
            ensure(userId > 0) { AppError.InvalidUserIdError(userId) }
            checkRole(role)
            context(userService, roleCheck) {
                doFindNameByUserId(id = userId, role = "admin")
            }
        }
        eow.shouldBeRight(User("User by it '$userId'"))
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
    data object InvalidRoleError : AppError("A role should not be empty!") {}
    data class InvalidUserIdError(val id: Int) : AppError("Invalid user ID: $id")
}
