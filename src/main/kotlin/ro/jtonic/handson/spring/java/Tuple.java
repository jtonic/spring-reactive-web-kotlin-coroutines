package ro.jtonic.handson.spring.java;

public interface Tuple {

    static <T1, T2> Tuple2<T1, T2> of(T1 first, T2 second) {
        return new Tuple2<>(first, second);
    }

    static <T1, T2, T3> Tuple3<T1, T2, T3> of(T1 first, T2 second, T3 third) {
        return new Tuple3<>(first, second, third);
    }
}
