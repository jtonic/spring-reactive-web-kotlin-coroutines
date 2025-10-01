package ro.jtonic.handson.spring.kotlin.coroutines

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

//language=JSON
private const val aPersonStr = """{ "name": "Tony" }"""

class WithMapperTest: FreeSpec ({

    val mapper: ObjectMapper = jacksonObjectMapper()

    data class Person(val name: String)

    "WithMapper 1" - {
        //language=JSON
        val result = mapper.readValue<Person>(aPersonStr)
        result.name shouldBe "Tony"
    }
    "WithMapper 2" - {
        val person: Person = mapper.read(aPersonStr)
        person.name shouldBe "Tony"
    }

    "WithMapper 3" - {
        mapper.withMapper {
            val result = readValue<Person>(aPersonStr)
            result.name shouldBe "Tony"
        }
    }
})

inline fun <reified T> ObjectMapper.read(value: String): T = run {
    withMapper {
         // many operations on object mappers here
         // but the last one must return T
         this.readValue(value, T::class.java)
    }
}

inline fun <reified T>  ObjectMapper.withMapper(f: ObjectMapper.() -> T) = run {
    f.invoke(this)
}
