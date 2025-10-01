package ro.jtonic.handson.spring.kotlin.coroutines;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.Map;

@ConfigurationProperties(prefix = "web")
public record WebApiProps(Map<String, WebClientProps> client) {
    public record WebClientProps(String name, String description, @DefaultValue("3") int retryCount) {
    }
}
