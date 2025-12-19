package ro.jtonic.handson.spring.java;

import io.opentelemetry.instrumentation.spring.autoconfigure.internal.instrumentation.kafka.KafkaInstrumentationAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = KafkaInstrumentationAutoConfiguration.class)
public class JavaMainApp {

    public static void main(String[] args) {
        SpringApplication.run(JavaMainApp.class, args);
    }
}
