package ro.jtonic.handson.spring.kotlin.coroutines;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "bbonb")
public record BBonbProps(Map<Templates, TemplateInfo> templates) {
    public enum Templates {
        INTERMEDIATE_REJECTION_SAVINGS, FINAL_REJECTION_SAVINGS
    }
    public record TemplateInfo(String template, String subject) {

    }
}


