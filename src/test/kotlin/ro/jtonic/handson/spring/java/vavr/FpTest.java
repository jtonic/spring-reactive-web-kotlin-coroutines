package ro.jtonic.handson.spring.java.vavr;

import io.vavr.API;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Test
    void testTry() {
        var res1 = Try.of(() -> {
            throw new IOException("Hello");
        });
        System.out.println("res1 = " + res1);
    }

    @Test
    void testNotThrowingException() {
        var res1 = Stream.of(1, 2, 3, 4)
                .map(API.unchecked(this::getFromFile))
                .filter(Objects::nonNull)
                .toList();
        System.out.println("res1 = " + res1);
        assertThat(res1).hasSize(2);
        assertThat(res1).isEqualTo(List.of(1, 2));
    }

    @Test
    void testThrowsException() {
        Assertions.assertThatThrownBy(() ->
                Stream.of(1, 2, 3, 4, 5).map(API.unchecked(this::getFromFile)).toList()
        ).isInstanceOf(IOException.class);
    }

    Integer getFromFile(Integer i) throws IOException {
        if(i >= 5) {
            throw new IOException("Hello");
        } else if (i >= 3) {
            return null;
        } else {
            return i;
        }
    }
}