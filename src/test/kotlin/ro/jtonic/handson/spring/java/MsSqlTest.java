package ro.jtonic.handson.spring.java;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import ro.jtonic.handson.spring.kotlin.coroutines.BBonbProps;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@JavaSpringIntegrationTest
@EnableConfigurationProperties({BBonbProps.class})
public class MsSqlTest {

    @Autowired
    private ApplicationContext ctx;

    @Test
    public void test() {
        assertNotNull(ctx);
    }
}
