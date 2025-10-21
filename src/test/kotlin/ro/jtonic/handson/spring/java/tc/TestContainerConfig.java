package ro.jtonic.handson.spring.java.tc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MSSQLServerContainer;
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


    //

    @Bean
    public MSSQLServerContainer<?> mssqlContainer(DynamicPropertyRegistry registry) {
        MSSQLServerContainer<?> mssql = new MSSQLServerContainer<>(
                DockerImageName.parse("mcr.microsoft.com/mssql/server:2022-latest")
        )
/*
                .withUsername("my_user")
                .withPassword("StrongP@ssw0rd123")
*/
                .withUsername("sa")
                .withPassword("Password!1a")
                .withDatabaseName("tempdb")
                .acceptLicense()
                .withReuse(true);
        registry.add("spring.datasource.url", mssql::getJdbcUrl);
        registry.add("spring.datasource.username", mssql::getUsername);
        registry.add("spring.datasource.password", mssql::getPassword);
        return mssql;
    }
}
