package ro.jtonic.handson.spring.kotlin.coroutines

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.asDeferred
import kotlinx.coroutines.future.future
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/customers")
class CustomerResource(
    private val customerService1: CustomerService1,
    private val customerService2: CustomerService2,
    private val customerService3: CustomerService3,
    private val runFlow: UseCase<Input, Unit>,
) {

    @GetMapping("/1")
    suspend fun getCustomers1(): List<Customer> =
        customerService1.getCustomers()

    @GetMapping("/2")
    suspend fun getCustomers2(): List<Customer> =
        customerService2.getCustomers().asDeferred().await()

    @OptIn(DelicateCoroutinesApi::class)
    @GetMapping("/3")
    fun getCustomers3(): CompletableFuture<List<Customer>> =
        GlobalScope.future { customerService3.getCustomers() }

    @PostMapping
    suspend fun postRunFlow(@RequestParam("input") input: Int): Unit = run {
        runFlow(Input(input))
    }
}

@Service
class CustomerService1 {
    suspend fun getCustomers() =
        listOf(
            Customer("1", "Antonel-Ernest Pazargic", 54),
            Customer("2", "Liviu Pazargic", 40),
            Customer("3", "Irina Pazargic", 33),
        )
}

@Service
class CustomerService2 {
    fun getCustomers(): CompletableFuture<List<Customer>> = CompletableFuture.completedFuture(
        listOf(
            Customer("10", "Antonel-Ernest Pazargic", 54),
            Customer("20", "Liviu Pazargic", 40),
            Customer("30", "Irina Pazargic", 33),
        )
    )
}

@Service
class CustomerService3 {
    suspend fun getCustomers() =
        listOf(
            Customer("100", "Antonel-Ernest Pazargic", 54),
        )
}
data class Customer(val id: String, val name: String, val age: Int)