package ro.jtonic.handson.spring.kotlin.coroutines;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {SpringKotlinCoroutinesApplication.class})
@ActiveProfiles(profiles = {"default", "tst"})
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
