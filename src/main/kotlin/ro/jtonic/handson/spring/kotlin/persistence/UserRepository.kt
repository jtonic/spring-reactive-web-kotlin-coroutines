package ro.jtonic.handson.spring.kotlin.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import ro.jtonic.handson.spring.kotlin.Generated

@Component
@Generated
interface UserRepository : JpaRepository<User, Long> {

    fun findByName(name: String): List<User>

    override fun findAll(): List<User>

    @get:Generated
    val usersCount: Int
        get() = findAll().size
}
