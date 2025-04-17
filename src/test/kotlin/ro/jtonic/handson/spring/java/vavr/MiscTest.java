package ro.jtonic.handson.spring.java.vavr;

import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class MiscTest {

    @Test
    public void testVavrTry() {
        Try.of(() -> {
          throw new RuntimeException("Boom!");
        }).orElseRun((exc) -> log.error("Boom!. Err message: {}", exc.getMessage()));
    }

    @Test
    public void testCF0() {
        CompletableFuture.completedFuture(new SSN("a"))
                .thenAccept((a) -> log.info("Success. Msg: {}", a.ssn))
                .exceptionally((e) -> {
                    log.error("[1] Exception occurred: {}", e.getMessage(), e);
                    return null;
                })
                .thenAccept(a -> {throw new RuntimeException("Boom!");})
                .exceptionally(e -> {
                    log.error("[2] Exception occurred: {}", e.getMessage(), e);
                    return null;
                })
                .join();
        assertTrue(true);
    }

    @Test
    public void testCF1() {
        CompletableFuture.<SSN>failedFuture(new RuntimeException("Boom!"))
                .thenAccept((a) -> log.info("Success. Msg: {}", a.ssn))
                .exceptionally((e) -> {
                    log.error("Exception occurred: {}", e.getMessage(), e);
                    return null;
                }).join();
    }

    @Test
    public void testCF2() {
        CompletableFuture.<SSN>failedFuture(new RuntimeException("Boom!"))
                .exceptionally(e -> {
                    log.error("Exception occurred: {}", e.getMessage(), e);
                    return null;
                })
                .thenAccept(a -> log.info("Success. Msg: {}", a != null ? a.ssn : "no-ssn"))
                .join();
    }

    public record SSN(String ssn) {}
}
