package ro.jtonic.handson.spring.java.vavr;

import io.vavr.control.Try;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MiscTest {

    private static final Logger log = LoggerFactory.getLogger(MiscTest.class);

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
    @Disabled
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
