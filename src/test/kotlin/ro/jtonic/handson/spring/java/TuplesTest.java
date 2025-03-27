package ro.jtonic.handson.spring.java;

import org.junit.jupiter.api.Test;
import ro.jtonic.handson.spring.java.nvavr.Tuple;
import ro.jtonic.handson.spring.java.nvavr.Tuple2;

import static org.assertj.core.api.Assertions.assertThat;

public class TuplesTest {

    @Test
    void testTuple2() {
        var tony = Tuple.of("Tony", 54);

        String result = switch (tony) {
            case Tuple2<String, Integer>(String first, Integer second) when first.equals("Tony") && second == 54 -> "Old Tony";
            case Tuple2<String, Integer>(String _ignored, Integer second) when second == 100 -> "Too old person";
            default -> "Unknown";
        };

        assertThat(tony.first()).isEqualTo("Tony");
        assertThat(tony.second()).isEqualTo(54);

        var tonyClone = new Tuple2<>("Tony", 54);
        assertThat(tonyClone).isEqualTo(tony);

        assertThat(result).isEqualTo("Old Tony");
    }
}
