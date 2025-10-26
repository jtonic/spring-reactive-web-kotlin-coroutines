package ro.jtonic.handson.spring.java;

//import io.opentelemetry.instrumentation.spring.autoconfigure.internal.instrumentation.kafka.KafkaInstrumentationAutoConfiguration;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import ro.jtonic.handson.spring.java.tc.TestContainersConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Testcontainers
@SpringBootTest(classes = {JavaMainApp.class})
@Import(TestContainersConfig.class)
@ActiveProfiles(profiles = {"default", "tst"})
//@EnableAutoConfiguration(exclude = {KafkaInstrumentationAutoConfiguration.class})
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface JavaSpringIntegrationTest {
}
