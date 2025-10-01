package ro.jtonic.handson.spring.kotlin.coroutines.kotest.newfeatures

import arrow.core.raise.Raise
import arrow.core.raise.either
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import ro.jtonic.handson.spring.kotlin.newfeatures.AppError
import ro.jtonic.handson.spring.kotlin.newfeatures.AppError.UserNotFoundError
import ro.jtonic.handson.spring.kotlin.newfeatures.UserInfo

class NewFeaturesTest : FreeSpec({

    "context parameters" {

        context(userService: UserService, roleCheckService: RoleCheckService, r: Raise<AppError>)
        fun doFindNameByUserId(userInfo: UserInfo): User =
            if (roleCheckService.check(userInfo.role)) {
                userService.findNameById(userInfo.id)
            } else {
                r.raise(UserNotFoundError)
            }

        val userService: UserService = object : UserService {
            override fun findNameById(id: Int): User =
                User(
                    userId = "user-id",
                    name = "Chuck Norris",
                    role = "admin"
                )
        }

        val roleCheck: RoleCheckService = object : RoleCheckService {
            override fun check(role: String): Boolean = role == "admin"
        }

        val userId = 1;
        val role = "admin"

        val eow = either {
            val userInfo = UserInfo.Companion(userId, role)
            context(userService, roleCheck) {
                doFindNameByUserId(userInfo)
            }
        }
        eow.shouldBeRight(
            User(
                userId = "user-id",
                name = "Chuck Norris",
                role = "admin"
            )
        )
    }
})

interface UserService {
    fun findNameById(id: Int): User
}

interface RoleCheckService {
    fun check(role: String): Boolean
}

data class User(val userId: String, val name: String, var role: String)
