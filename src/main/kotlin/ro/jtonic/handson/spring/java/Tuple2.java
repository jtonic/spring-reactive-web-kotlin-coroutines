package ro.jtonic.handson.spring.java;

public record Tuple2<T1, T2>(T1 first, T2 second) {

    public static <T1, T2> Tuple2<T1, T2> of(T1 a1, T2 a2) {
        return new Tuple2<>(a1, a2);
    }
}
