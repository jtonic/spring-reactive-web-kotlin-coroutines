package ro.jtonic.handson.spring.kotlin.coroutines.junit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ro.jtonic.handson.spring.java.JavaSpringIntegrationTest;
import ro.jtonic.handson.spring.kotlin.coroutines.WebApiProps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@JavaSpringIntegrationTest
@EnableConfigurationProperties({WebApiProps.class})
public class WebApiPropsTest {

    @Autowired
    private WebApiProps webApiProps;

    @Test
    void test() {
        assertNotNull(webApiProps);
        var client = webApiProps.client();
        assertThat(client).hasSize(2);
        assertThat(client.get("one").retryCount()).isEqualTo(3);
        assertThat(client.get("two").retryCount()).isEqualTo(5);
    }
}
