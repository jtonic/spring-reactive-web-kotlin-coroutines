package ro.jtonic.handson.spring.kotlin.coroutines

import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.util.ResourceUtils
import reactor.netty.http.client.HttpClient
import java.io.FileInputStream
import java.security.KeyStore
import javax.net.ssl.KeyManagerFactory

@Configuration
class TestSslConfiguration {

    @Value("\${server.ssl.key-store}")
    private lateinit var keyStoreLocation: String

    @Value("\${server.ssl.key-store-type}")
    private lateinit var keyStoreType: String

    @Value("\${server.ssl.key-store-password}")
    private lateinit var keyStorePassword: String

    @Value("\${server.ssl.key-store-password}")
    private lateinit var keyPassword: String

    private fun keyStoreInstance(): KeyStore = KeyStore.getInstance(keyStoreType).apply {
        FileInputStream(ResourceUtils.getFile(keyStoreLocation)).use { inputStream ->
            load(inputStream, keyStorePassword.toCharArray())
        }
    }

    private fun keyManagerFactory() = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm()).apply {
        init(keyStoreInstance(), keyPassword.toCharArray())
    }

    private fun sslContext() = SslContextBuilder.forClient()
        .keyManager(keyManagerFactory())
        .trustManager(InsecureTrustManagerFactory.INSTANCE)
        .build()

    @Bean
    fun clientConnector(): ReactorClientHttpConnector {
        val httpClient =
            HttpClient.create().secure { it.sslContext(sslContext()) }
        return ReactorClientHttpConnector(httpClient)
    }

    @Bean
    fun webTestClientBuilder(clientConnector: ReactorClientHttpConnector): WebTestClient.Builder {
        return WebTestClient.bindToServer(clientConnector)
    }
}
