package functional;

public class TMulti {
}

@FunctionalInterface
interface TriFunction<T, U, V, R> {
    R apply(T t, U u, V v);
}

class TriFunctionTest {
    static long f(int i, long l, double d) {
        System.out.println(":::::::::: " + d);
        return 99;
    }

    public static void main(String[] args) {
        TriFunction<Integer, Long, Double, Long> tf = TriFunctionTest::f;
        tf = (i, l, d) -> l * 2;
        System.out.println(tf.apply(22, 12L, 1.0));
    }
}
