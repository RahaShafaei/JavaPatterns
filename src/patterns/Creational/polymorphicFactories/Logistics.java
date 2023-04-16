package patterns.Creational.polymorphicFactories;


import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Logistics {
    public void movement(String cargo) {
        System.out.println("Move " + cargo + " by " + this.getClass().getSimpleName());
    }

    public void transportation(String cargo) {
        System.out.println("Transfer " + cargo + " by " + this.getClass().getSimpleName());
    }

    public void storage(String cargo) {
        System.out.println("Store " + cargo + " by " + this.getClass().getSimpleName());
    }
}

class RoadLogistics extends Logistics {

}

class AirLogistics extends Logistics {

}

class SeaLogistics extends Logistics {

}

interface ChooseLogistics {
    Logistics create();
}

class RandomLogistics implements Supplier<Logistics> {
    private final ChooseLogistics[] chooseLogistics;
    private Random rand = new Random(42);

    RandomLogistics(ChooseLogistics... chooseLogistics) {
        this.chooseLogistics = chooseLogistics;
    }

    @Override
    public Logistics get() {
        return chooseLogistics[rand.nextInt(chooseLogistics.length)].create();
    }
}

class ImplementLogistics {
    static int i;

    public static void main(String[] args) {
        RandomLogistics randomLogistics = new RandomLogistics(
                SeaLogistics::new,
                AirLogistics::new,
                RoadLogistics::new
        );

        Stream.generate(randomLogistics).limit(6).forEach(o -> {
            String cargo = "CARGO" + ++i;
            System.out.println(":::::::::::::::::::::::::::::::::::");
            o.transportation(cargo);
            o.movement(cargo);
            o.storage(cargo);
        });
    }
}