package ro.jtonic.handson.spring.kotlin.newfeatures

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.equals.shouldBeEqual

class NewFeaturesSpec : FreeSpec({

    "context parameters" {

        context(userService: UserService, roleCheckService: RoleCheckService)
        fun doFindNameByUserId(id: Int, role: String) =
            if (roleCheckService.check(role)) {
                userService.findNameById(id)
            } else {
                null
            }

        val userService: UserService = object : UserService {
            override fun findNameById(id: Int): String =
                "Found by userId = $id"
        }

        val roleCheck: RoleCheckService = object : RoleCheckService {
            override fun check(role: String): Boolean = role == "admin"
        }

        context(userService, roleCheck) {
            val userId = 1;
            val result = doFindNameByUserId(id = userId, role = "admin")
            result?.shouldBeEqual("Found by userId = $userId")
        }
    }
})

interface UserService {
    fun findNameById(id: Int): String
}

interface RoleCheckService {
    fun check(role: String): Boolean
}
