package ro.jtonic.handson.spring.java.aspect;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration(exclude = KafkaAutoConfiguration.class)
public class TestConfig {
}
