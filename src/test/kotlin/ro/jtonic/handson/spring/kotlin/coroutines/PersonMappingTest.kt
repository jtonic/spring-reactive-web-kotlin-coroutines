package ro.jtonic.handson.spring.kotlin.coroutines

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.mapstruct.InheritInverseConfiguration
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.factory.Mappers
import java.time.LocalDate

class PersonMappingTest : FreeSpec({

    "mapping person to and from dto" {
        val mapper = Mappers.getMapper(PersonMapper::class.java)
        val birthdate = LocalDate.of(1970, 1, 29)
        val person = Person("Tony", "Paza", "1234", birthdate)

        val dto = mapper.toDto(person)
        dto.firstName shouldBe "Tony"
        dto.lastName shouldBe "Paza"
        dto.phone shouldBe "1234"
        dto.birthdate shouldBe birthdate

        mapper.toModel(dto) shouldBe person
    }

    "mapping user to and from dto" {
        val mapper = Mappers.getMapper(UserMapper::class.java)
        val user = User("ktonic", Role("admin"))

        val dto = mapper.toDto(user)

        dto.role shouldBe user.role.name
        dto.uname shouldBe user.userName

        mapper.toModel(dto) shouldBe user
    }
})

data class User(val userName: String, val role: Role)
data class Role(val name: String)

data class UserDto(val uname: String, val role: String)

@Mapper
interface UserMapper {

    @Mappings(
        Mapping(source = "userName", target = "uname"),
        Mapping(source = "role.name", target = "role")
    )
    fun toDto(user: User) : UserDto

    @InheritInverseConfiguration
    fun toModel(userDto: UserDto) : User
}
