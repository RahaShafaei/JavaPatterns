package patterns.Creational.factoryMethod;

import java.util.stream.*;

public class Weapon {
    public void makeFire() {
        System.out.println(this.getClass().getSimpleName() + " makes fire!");
    }

    public void makeSound() {
        System.out.println(this.getClass().getSimpleName()  + " makes Sound!");
    }
}

class BadShapeCreation extends RuntimeException {
    public BadShapeCreation(String msg) {
        super(msg);
    }
}

class Orcish extends Weapon {
}

class Elvish extends Weapon {
}

class Customer {
    void useWeapon(Weapon weapon) {
        System.out.println("Customer : " + this.getClass().getSimpleName() +
                ", use : " + weapon.getClass().getSimpleName() + " weapon.");
    }
}

class Elves extends Customer {
}

class Orcs extends Customer {
}

interface FactoryMethodWeapon {
    Weapon create(String msg);
}

interface FactoryMethodCustomer {
    Customer create(String msg);
}

class FactoryTest {
    public static void test(FactoryMethodWeapon fw/*, FactoryMethodCustomer c*/) {
        Stream.of("Elvish", "Orcish", "Elvish", "Elvish", "Orcish")
                .map(fw::create)
                .forEach(w -> {w.makeFire(); w.makeSound();});

//        Stream.of("Elvish", "Orcish", "Elvish", "Elvish", "Orcish")
//                .map(fw::create)
//                .peek(Weapon::makeFire)
//                .peek(Weapon::makeSound)
//                .count(); // Terminal operation
    }
}

class UseFactories implements FactoryMethodWeapon {
    @Override
    public Weapon create(String type) {
        switch (type) {
            case "Elvish":
                return new Elvish();
            case "Orcish":
                return new Orcish();
            default:
                throw new BadShapeCreation(type);
        }
    }

    public static void main(String[] args) {
        FactoryTest.test(new UseFactories());

    }
}

