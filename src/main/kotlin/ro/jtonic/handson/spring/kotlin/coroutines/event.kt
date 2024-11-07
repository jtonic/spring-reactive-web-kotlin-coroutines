package ro.jtonic.handson.spring.kotlin.coroutines

import io.github.oshai.kotlinlogging.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener

@Configuration
class CustomerEventsListerConfig {
    @Bean
    fun customerNotificationListener() = CustomerEventNotificationLister()

    @Bean
    fun customerSaveListener() = CustomerEventSaveLister()
}

class CustomerEventSaveLister {

    private val log = KotlinLogging.logger {}

    @EventListener
    fun handle(customer: Customer) {
        log.info { "[Save] Received customer event: $customer" }
    }
}
class CustomerEventNotificationLister {

    private val log = KotlinLogging.logger {}

    @EventListener
    fun handle(customer: Customer) {
        log.info { "[Notification] Received customer event: $customer" }
    }
}
