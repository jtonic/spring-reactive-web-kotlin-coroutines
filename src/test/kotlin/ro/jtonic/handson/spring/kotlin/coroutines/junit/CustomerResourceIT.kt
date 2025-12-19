package ro.jtonic.handson.spring.kotlin.coroutines.junit

import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import ro.jtonic.handson.spring.kotlin.coroutines.KotlinSpringIntegrationTest
import kotlin.test.Test

@KotlinSpringIntegrationTest
class CustomerResourceIT(
    private val webTestClientBuilder: WebTestClient.Builder,
    @LocalServerPort private val port: Int,
) {
    private val webTestClient by lazy {
        webTestClientBuilder.baseUrl("https://localhost:$port").build()
    }

    @Test
    fun getCustomers() {
        webTestClient
            .post()
            .uri("/customers")
            .body(BodyInserters.fromFormData("input", "10"))
            .exchange()
            .expectStatus().isOk()
    }
}