package ro.jtonic.handson.spring.kotlin.newfeatures

import arrow.core.raise.Raise
import arrow.core.raise.ensure

sealed class AppError(msg: String) {
    data object UserNotFoundError : AppError("User not found") {}
    data object InvalidRoleError : AppError("A role should not be empty!") {}
    data class InvalidUserIdError(val id: Int) : AppError("Invalid user ID: $id")
}

data class UserInfo private constructor(val id: Int, val role: String) {

    companion object {
        context(r: Raise<AppError>)
        operator fun invoke(id: Int, role: String): UserInfo = run {
            r.ensure(id > 0) { AppError.InvalidUserIdError(id) }
            r.ensure(role.isNotEmpty()) { AppError.InvalidRoleError }
            UserInfo(id, role)
        }
    }
}
