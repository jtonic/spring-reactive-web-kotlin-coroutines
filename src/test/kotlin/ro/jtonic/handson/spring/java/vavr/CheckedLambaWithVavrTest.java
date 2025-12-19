package ro.jtonic.handson.spring.java.vavr;

import io.vavr.API;
import io.vavr.CheckedFunction1;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.stream.Stream;

public class CheckedLambaWithVavrTest {

    @Test
    void testThrowsException() {
        Assertions.assertThatThrownBy(() ->
                Stream.of(1, 2, 3, 4, 5).map(API.unchecked(this::getFromFile)).toList()
        ).isInstanceOf(IOException.class);
    }

    @Test
    void liftCheckedExceptionTest() {
        var list = Stream.of(5)
                .map(CheckedFunction1.lift(this::getFromFile))
                .map(k -> k.getOrElse(-1))
                .toList();
        System.out.println("list = " + list);
    }

    Integer getFromFile(Integer i) throws IOException {
        if (i >= 5) {
            throw new IOException("Hello");
        } else if (i >= 3) {
            return null;
        } else {
            return i;
        }
    }
}
