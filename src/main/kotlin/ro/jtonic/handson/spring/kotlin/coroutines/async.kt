package ro.jtonic.handson.spring.kotlin.coroutines

import org.apache.coyote.ProtocolHandler
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.AsyncTaskExecutor
import org.springframework.core.task.support.TaskExecutorAdapter
import org.springframework.scheduling.annotation.EnableAsync
import java.util.concurrent.Executors

@EnableAsync
@Configuration
@ConditionalOnProperty(
    "spring.threads.virtual.enabled",
    havingValue = "true"
)
class ThreadConfig {
    @Bean
    fun  applicationTaskExecutor(): AsyncTaskExecutor =
        TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor())

    @Bean
    fun <T : ProtocolHandler> protocolHandlerVirtualThreadExecutorCustomizer():  TomcatProtocolHandlerCustomizer<T> =
        TomcatProtocolHandlerCustomizer {
            it.executor = Executors.newVirtualThreadPerTaskExecutor()
        }
}