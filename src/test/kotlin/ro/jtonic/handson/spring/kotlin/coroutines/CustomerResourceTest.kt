package ro.jtonic.handson.spring.kotlin.coroutines

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerResourceTest (
    @Autowired private  val webTestClientBuilder: WebTestClient.Builder,
    @LocalServerPort val port: Int
) {
    private val webTestClient by lazy {
        webTestClientBuilder.baseUrl("https://localhost:$port").build()
    }

    @Test
    fun getCustomers() {
        webTestClient
            .get()
            .uri("/customers")
            .exchange()
            .expectStatus().isOk()
    }
}