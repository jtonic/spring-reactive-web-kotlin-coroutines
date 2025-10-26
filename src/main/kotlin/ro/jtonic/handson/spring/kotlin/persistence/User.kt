package ro.jtonic.handson.spring.kotlin.persistence

import jakarta.persistence.*

@Entity
@Table(name = "Users")
class User(
    @Column(name = "Role") private var role: String
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private var id: Long? = null

    @Column(name = "Name")
    private lateinit var name: String
}
