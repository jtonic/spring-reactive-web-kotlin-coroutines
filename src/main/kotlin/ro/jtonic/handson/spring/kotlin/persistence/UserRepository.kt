package ro.jtonic.handson.spring.kotlin.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

@Component
interface UserRepository : JpaRepository<User, Long> {

    fun findByName(name: String): List<User>

    override fun findAll(): List<User>

    val usersCount: Int
        get() = findAll().size
}
