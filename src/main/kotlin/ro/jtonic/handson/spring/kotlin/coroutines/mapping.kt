package ro.jtonic.handson.spring.kotlin.coroutines

import org.mapstruct.InheritInverseConfiguration
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import java.time.LocalDate

data class Person(var firstName: String?, var lastName: String?, var phoneNumber: String?, var birthdate: LocalDate?)

data class PersonDto(var firstName: String?, var lastName: String?, var phone: String?, var birthdate: LocalDate?)

@Mapper
interface PersonMapper {

    @Mapping(source = "phoneNumber", target = "phone")
    fun toDto(person: Person) : PersonDto

    @InheritInverseConfiguration
    fun toModel(personDto: PersonDto) : Person
}