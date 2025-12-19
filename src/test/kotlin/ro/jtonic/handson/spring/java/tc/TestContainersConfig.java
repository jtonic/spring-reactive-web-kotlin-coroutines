package ro.jtonic.handson.spring.java.tc;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.util.TestSocketUtils;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.containers.startupcheck.MinimumDurationRunningStartupCheckStrategy;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;
import java.time.Duration;

@Configuration
public class TestContainersConfig {

    @Bean
    public KafkaContainer kafkaContainer(DynamicPropertyRegistry registry) {
        KafkaContainer kafka = new KafkaContainer(
                DockerImageName.parse("confluentinc/cp-kafka:7.6.1")
                        .asCompatibleSubstituteFor("apache/kafka")
        ).withReuse(true);
        registry.add("tc.kafka.bootstrap-servers", kafka::getBootstrapServers);
        return kafka;
    }

    @Bean
    public MSSQLServerContainer<?> mssql(DynamicPropertyRegistry registry) {
        int port = TestSocketUtils.findAvailableTcpPort();
        MSSQLServerContainer<?> mssql = new MSSQLServerContainer<>(
                DockerImageName.parse("mcr.microsoft.com/azure-sql-edge:latest") // arm64 compatible image
                        .asCompatibleSubstituteFor("mcr.microsoft.com/mssql/server")
//                DockerImageName.parse("mcr.microsoft.com/mssql/server") // only amd64 image (orig)
        )
                .acceptLicense()
                .withStartupCheckStrategy(new MinimumDurationRunningStartupCheckStrategy(Duration.ofSeconds(10)))
                .withPassword("Password!1a")
                .withInitScripts("sql/init.sql")
                .withReuse(true);
        mssql.addExposedPort(port);
        mssql.start();
        return mssql;
    }


    @Primary
    @Bean(name = "dataSource")
    public DataSource dataSource(@Qualifier("mssql") MSSQLServerContainer<?> db) {
        return DataSourceBuilder.create()
                .driverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
                .username("sa")
                .password("Password!1a")
                .url("jdbc:sqlserver://localhost:%d;database=KkmkDB;trustServerCertificate=true".formatted(db.getMappedPort(1433)))
                .build();
    }

    @FlywayDataSource
    @Bean(name = "flywayDataSource")
    public DataSource flywayDataSource(@Qualifier("mssql") MSSQLServerContainer<?> db) {
        return DataSourceBuilder.create()
                .driverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
                .username("sa")
                .password("Password!1a")
                .url("jdbc:sqlserver://localhost:%d;database=KkmkDB;trustServerCertificate=true".formatted(db.getMappedPort(1433)))
                .build();
    }
}
