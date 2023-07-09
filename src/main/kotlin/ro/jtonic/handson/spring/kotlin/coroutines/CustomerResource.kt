package ro.jtonic.handson.spring.kotlin.coroutines

import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/customers")
class CustomerResource(private val customerService: CustomerService) {

    @GetMapping
    suspend fun getCustomers(): List<Customer> =
        customerService.getCustomers()
}

@Service
class CustomerService {
    suspend fun getCustomers() =
        listOf(
            Customer("1", "Antonel-Ernest Pazargic", 53),
            Customer("2", "Liviu Pazargic", 40),
            Customer("3", "Irina Pazargic", 33),
        )
}

data class Customer(val id: String, val name: String, val age: Int)