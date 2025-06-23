package ro.jtonic.handson.spring.java.tc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@Configuration
public class TestContainerConfig {

    @Bean
    public KafkaContainer kafkaContainer(DynamicPropertyRegistry registry) {
        KafkaContainer kafka = new KafkaContainer(
                DockerImageName.parse("confluentinc/cp-kafka:7.6.1")
                        .asCompatibleSubstituteFor("apache/kafka")
        ).withReuse(true);
        registry.add("tc.kafka.bootstrap-servers", kafka::getBootstrapServers);
        return kafka;
    }
}
