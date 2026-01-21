package ro.jtonic.handson.spring.kotlin.coroutines.kotest

import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.FreeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldNotBe
import org.springframework.context.ApplicationContext
import ro.jtonic.handson.spring.kotlin.coroutines.KotlinSpringIntegrationTest

@KotlinSpringIntegrationTest
@ApplyExtension(extensions = [SpringExtension::class])
class SpringBootTest(
    val appCtx: ApplicationContext
) : FreeSpec({

        "test spring context loads" {
            appCtx shouldNotBe null
        }
})