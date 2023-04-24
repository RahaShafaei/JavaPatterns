package patterns.behavioral.templateMethod;

import java.util.*;

public abstract class GeneralSteal {
    public GeneralSteal(StealHelper details) {
        templateMethod(details);
    }

    public abstract void pickTarget(String target);

    public abstract void confuse(String who);

    public abstract void stealItem(String item);

    private void templateMethod(StealHelper details) {
        pickTarget(details.getTarget());
        confuse(details.getWho());
        stealItem(details.getItem());
    }
}

class BadCreation extends RuntimeException {
    public BadCreation(String msg) {
        super(msg);
    }
}

class StealHelper {
    String typ, target, who, item;

    public StealHelper(String typ, String target, String who, String item) {
        this.typ = typ;
        this.target = target;
        this.who = who;
        this.item = item;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}

class StealBank extends GeneralSteal {
    public StealBank(StealHelper details) {
        super(details);
    }

    @Override
    public void pickTarget(String target) {
        System.out.println("Bank target: " + target);
    }

    @Override
    public void confuse(String who) {
        System.out.println("Bank who: " + who);
    }

    @Override
    public void stealItem(String item) {
        System.out.println("Bank item: " + item);
    }
}

class StealHome extends GeneralSteal {
    public StealHome(StealHelper details) {
        super(details);
    }

    @Override
    public void pickTarget(String target) {
        System.out.println("Home target: " + target);
    }

    @Override
    public void confuse(String who) {
        System.out.println("Home who: " + who);
    }

    @Override
    public void stealItem(String item) {
        System.out.println("Home item: " + item);
    }
}

interface StealFactory {
    GeneralSteal create(StealHelper stealHelpers);
}

class FactoryTest {

    public static void test(StealFactory stealFactory) {
        List<StealHelper> stealHelpers = new ArrayList<>();
        stealHelpers.add(new StealHelper("Bank", "SmithBANK", "John", "Cach"));
        stealHelpers.add(new StealHelper("Bank", "SaraBANK", "Jack", "Golden"));
        stealHelpers.add(new StealHelper("Home", "YalomHOME", "Sam", "AllStaff"));
        stealHelpers.add(new StealHelper("Bank", "StateBANK", "ProfessionalGroup", "AllStaff"));

        stealHelpers.stream().forEach(stealFactory::create);
    }
}

class UseFactory implements StealFactory {
    @Override
    public GeneralSteal create(StealHelper stealHelpers) {
        return switch (stealHelpers.getTyp()) {
            case "Bank" -> new StealBank(stealHelpers);
            case "Home" -> new StealHome(stealHelpers);
            default -> throw new BadCreation(stealHelpers.getTyp());
        };
    }

    public static void main(String[] args) {
        FactoryTest.test(new UseFactory());
    }
}
