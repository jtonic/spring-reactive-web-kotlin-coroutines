package ro.jtonic.handson.spring.kotlin.coroutines;

import io.vavr.control.Option;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.function.Function;

public class FpTest {

    @Test
    void testNeg() {
        Option<Integer> opt2 = Option.ofOptional(Optional.ofNullable(null));
        System.out.println("opt2 = " + opt2);
        var try2 = opt2.toTry();
        System.out.println("try2 = " + try2);
        var either2 = opt2.toEither("No Value");
        System.out.println("either2 = " + either2);
        var res2 = either2.fold(
                lf -> -1,
                Function.identity()
        );
        System.out.println("res2 = " + res2);
    }

    @Test
    void testPoz() {
        var opt1 = Option.ofOptional(Optional.of("Hello"));
        System.out.println("opt1 = " + opt1);
        var try1 = opt1.toTry();
        System.out.println("try1 = " + try1);
        var either1 = try1.toEither();
        System.out.println("either1 = " + either1);
        var res1 = either1.fold(
                l -> -1,
                Function.identity()
        );
        System.out.println("res1 = " + res1);
    }
}
