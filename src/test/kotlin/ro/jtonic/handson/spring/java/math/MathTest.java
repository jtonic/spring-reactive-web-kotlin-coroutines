package ro.jtonic.handson.spring.java.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class MathTest {

    @Test
    void test() {
        var a = BigDecimal.ZERO.compareTo(new BigDecimal(-1)) > 0;
        Assertions.assertTrue(a);
    }
}
