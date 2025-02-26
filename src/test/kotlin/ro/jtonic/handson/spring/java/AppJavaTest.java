package ro.jtonic.handson.spring.java;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import ro.jtonic.handson.spring.kotlin.coroutines.BBonbProps;
import ro.jtonic.handson.spring.kotlin.coroutines.BBonbProps.Templates;
import ro.jtonic.handson.spring.kotlin.coroutines.SpringKotlinCoroutinesApplication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {SpringKotlinCoroutinesApplication.class})
@ActiveProfiles(profiles = {"default", "tst"})
public class AppJavaTest {

    @Autowired
    private ApplicationContext ctx;

    @Value("${bbonb.message}")
    private String msg;

    @Autowired
    private BBonbProps props;

    @Test
    public void test() {
        assertNotNull(ctx);
        assertEquals("Hello", msg);
        assertThat(props.templates()).hasSize(2);
        System.out.println("props.cases() = " + props.templates());

        assertThat(props.templates().get(Templates.FINAL_REJECTION_SAVINGS).template())
                .isEqualTo("bbonb/Template/bbonb_savings_final_rejection");
    }
}
