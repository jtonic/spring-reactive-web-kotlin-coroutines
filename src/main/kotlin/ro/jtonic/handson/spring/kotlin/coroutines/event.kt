package ro.jtonic.handson.spring.kotlin.coroutines

import io.github.oshai.kotlinlogging.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async

@Configuration
class CustomerEventsListerConfig {

//    @Bean("threadPoolTaskExecutor")
//    fun threadPoolTaskExecutor(): Executor =
//        Executors.newVirtualThreadPerTaskExecutor()

    @Bean
    fun customerNotificationListener() = CustomerEventNotificationLister()

    @Bean
    fun customerSaveListener() = CustomerEventSaveLister()
}

open class CustomerEventSaveLister {

    private val log = KotlinLogging.logger {}

    //@Async("threadPoolTaskExecutor")
    @Async
    @EventListener
    open fun handle(customer: Customer) {
        log.info { "[Save] Received customer event: $customer" }
    }
}

open class CustomerEventNotificationLister {

    private val log = KotlinLogging.logger {}

    //@Async("threadPoolTaskExecutor")
    @Async
    @EventListener
    open fun handle(customer: Customer) {
        log.info { "[Notification] Received customer event: $customer" }
    }
}
