package patterns.behavioral.state;

public class Mammoth {
    Behavior behavior;

    public Mammoth(Behavior behavior) {
        this.behavior = behavior;
    }

    public Behavior getBehavior() {
        return behavior;
    }

    @Override
    public String toString() {
        return "Mammoth{" +
                "name='" + behavior + '\'' +
                '}';
    }
}

class Behavior {
    Behavior situation;
    protected Behavior() {}

    public Behavior(Behavior situation) {
        this.situation = situation;
    }

    void changeSituation(Behavior situation) {
        this.situation = situation;
    }

    void walking() {
        situation.walking();
    }

    void makeSound() {
        situation.makeSound();
    }

    void breathing() {
        situation.breathing();
    }

    @Override
    public String toString() {
        return "Behavior{" +
                "situation=" + situation +
                '}';
    }
}

class DangerSituation extends Behavior {
    @Override
    void walking() {
        System.out.println(this.getClass().getSimpleName() + ": Walking angry!");
    }

    @Override
    void makeSound() {
        System.out.println(this.getClass().getSimpleName() + ": MakeSound angry!");
    }

    @Override
    void breathing() {
        System.out.println(this.getClass().getSimpleName() + ": Breathing angry!");
    }

}

class CalmSituation extends Behavior {
    @Override
    void walking() {
        System.out.println(this.getClass().getSimpleName() + ": Walking happy!");
    }

    @Override
    void makeSound() {
        System.out.println(this.getClass().getSimpleName() + ": MakeSound happy!");
    }

    @Override
    void breathing() {
        System.out.println(this.getClass().getSimpleName() + ": Breathing happy!");
    }
}

class AdayOfMammoth {
    public static void main(String[] args) {
        Mammoth mammoth = new Mammoth(new Behavior(new DangerSituation()));
        mammoth.getBehavior().breathing();
        mammoth.getBehavior().makeSound();
        mammoth.getBehavior().walking();
        System.out.println(":::::::::::::::::::::::::::");
        mammoth.getBehavior().changeSituation(new CalmSituation());
        mammoth.getBehavior().breathing();
        mammoth.getBehavior().makeSound();
        mammoth.getBehavior().walking();
    }
}


