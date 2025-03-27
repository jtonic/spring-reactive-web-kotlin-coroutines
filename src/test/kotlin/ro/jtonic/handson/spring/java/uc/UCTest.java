package ro.jtonic.handson.spring.java.uc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UCTest {

    @FunctionalInterface
    public interface Op<I, R> {
        R execute(I i);
    }

    @FunctionalInterface
    public interface Command<I, R> {
        R call(I i);
    }

    public static <I extends Op<Void, String>> String simpleExec(Void a, I fn) {
        return fn.execute(a);
    }

    public static <I extends Op<Void, String> & Command<String, String>> String exec(Void a, I fn) {
        var res1 = fn.execute(a);
        var res2 = fn.call(res1);
        return res2;
    }

    public static class MyOp1 implements Op<Void, String> {

        @Override
        public String execute(Void unused) {
            return "Hello";
        }
    }

    class Ops implements Op<Void, String>, Command<String, String> {

        @Override
        public String call(String s) {
            return s + s;
        }

        @Override
        public String execute(Void unused) {
            return "Hello";
        }
    }

    @Test
    void testSimpleExec() {
        var res = simpleExec(null, $_ -> "Hello");
        assertEquals("Hello", res);
    }

    @Test
    void testExc() {
        var res = exec(null, new Ops());
        assertEquals("HelloHello", res);
    }

    @Test
    void testExc2() {
        class Consumer1<I extends Op<Void, String> & Command<String, String>> {

            private final I fn;

            public Consumer1(I fn) {
                this.fn = fn;
            }

            public String exec(Void a) {
                var res1 = fn.execute(a);
                var res2 = fn.call(res1);
                return res2;
            }
        }
        var res1 = new Consumer1(new Ops()).exec(null);
        assertEquals("HelloHello", res1);
    }

    @Test
    void test() {
        Op<Void, String> op1 = $n -> "Hello";
        assertEquals("Hello", op1.execute(null));

        MyOp1 op2 = new MyOp1();
        assertEquals("Hello", op2.execute(null));
    }
}
