package patterns.behavioral.strategy;

import java.util.function.Consumer;

class ChooseMethod {
    Consumer<Dragon> method;
}

class Slaying extends ChooseMethod {

}

class GunSlaying extends Slaying {
    public GunSlaying() {
        method = (dragon) -> {
            System.out.println("Slaying " + dragon + " by GUN");
        };
    }
}

class PoisonSlaying extends Slaying {
    public PoisonSlaying() {
        method = (dragon) -> {
            System.out.println("Slaying " + dragon + " by POISON");
        };
    }
}

class TrapSlaying extends Slaying {
    public TrapSlaying() {
        method = (dragon) -> {
            System.out.println("Slaying " + dragon + " by TRAP");
        };
    }
}

class Dragon {
    String name;

    public Dragon(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Dragon{" +
                "name='" + name + '\'' +
                '}';
    }
}

public class SlayingDragons {
    private ChooseMethod strategy;

    public SlayingDragons(ChooseMethod strategy) {
        this.strategy = strategy;
    }

    void method(Dragon dragon){
        strategy.method.accept(dragon);
    }

    void changeMethod(ChooseMethod strategy){
        this.strategy = strategy;
    }

    public static void main(String[] args) {
        Dragon dragon = new Dragon("Huge Dragon");

        SlayingDragons slayingDragons = new SlayingDragons(new GunSlaying());
        slayingDragons.method(dragon);

        slayingDragons.changeMethod(new PoisonSlaying());
        slayingDragons.method(dragon);

        slayingDragons.changeMethod(new TrapSlaying());
        slayingDragons.method(dragon);

    }
}
