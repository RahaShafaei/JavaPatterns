package patterns.Creational.multipleDispatching;

import java.util.Arrays;
import java.util.List;
import java.util.SplittableRandom;
import java.util.function.Supplier;
import java.util.stream.Stream;

enum Colors {
    RED,
    GREEN,
    BLUE,
    CYAN,
    MAGENTA,
    YELLOW
}

interface Color {
    Colors combining(Color color);

    Colors merge(Red red);

    Colors merge(Green green);

    Colors merge(Blue blue);
}

class Red implements Color {
    @Override
    public Colors combining(Color color) {
        return color.merge(this);
    }

    @Override
    public Colors merge(Red red) {
        return Colors.RED;
    }

    @Override
    public Colors merge(Green green) {
        return Colors.YELLOW;
    }

    @Override
    public Colors merge(Blue blue) {
        return Colors.MAGENTA;
    }

    @Override
    public String toString() {
        return "RED";
    }
}

class Green implements Color {
    @Override
    public Colors combining(Color color) {
        return color.merge(this);
    }

    @Override
    public Colors merge(Red red) {
        return Colors.YELLOW;
    }

    @Override
    public Colors merge(Green green) {
        return Colors.GREEN;
    }

    @Override
    public Colors merge(Blue blue) {
        return Colors.CYAN;
    }

    @Override
    public String toString() {
        return "GREEN";
    }
}

class Blue implements Color {
    @Override
    public Colors combining(Color color) {
        return color.merge(this);
    }

    @Override
    public Colors merge(Red red) {
        return Colors.MAGENTA;
    }

    @Override
    public Colors merge(Green green) {
        return Colors.CYAN;
    }

    @Override
    public Colors merge(Blue blue) {
        return Colors.BLUE;
    }

    @Override
    public String toString() {
        return "BLUE";
    }
}

class Tuple<A, B> {
    public final A a1;
    public final B a2;

    public Tuple(A a, B b) {
        a1 = a;
        a2 = b;
    }

    public String rep() {
        return a1 + ", " + a2;
    }

    @Override
    public String toString() {
        return "(" + rep() + ")";
    }
}

class ColorFactory {
    static List<Supplier<Color>> colors = Arrays.asList(Red::new, Green::new, Blue::new);
    static final int SZ = colors.size();
    private static SplittableRandom rand = new SplittableRandom(47);

    public static Color newColor() {
        return colors.get(rand.nextInt(SZ)).get();
    }

    public static Tuple<Color, Color> newPair() {
        return new Tuple(newColor(), newColor());
    }
}

class Combination {
    public static Colors combine(Tuple<Color, Color> p) {
        System.out.print(p.a1 + " -> " + p.a2 + " : ");
        return p.a1.combining(p.a2);
    }
}

public class ColorCombining {
    public static void main(String[] args) {
        Stream.generate(ColorFactory::newPair)
                .limit(20)
                .map(Combination::combine)
                .forEach(System.out::println);
    }
}
