package patterns.structural.proxy;

import java.util.ArrayList;
import java.util.List;

public abstract class Tower implements BaseTower {
    @Override
    public abstract void enterTower(WizardsType wizardsType, String msg);

    @Override
    public abstract void readSpell(WizardsType wizardsType, String msg);

    public void exitTower(WizardsType wizardsType, String msg) {
        System.out.println("Exit Tower{}");
    }

    @Override
    public String toString() {
        return "Tower{}";
    }
}

class IvoryTower extends Tower {
    @Override
    public void enterTower(WizardsType wizardsType, String msg) {
        System.out.println(wizardsType + " by " + msg + " enter \"IvoryTower\"");
    }

    @Override
    public void readSpell(WizardsType wizardsType, String msg) {
        System.out.println(wizardsType + " by " + msg + " read spell in \"IvoryTower\"");
    }

    @Override
    public void exitTower(WizardsType wizardsType, String msg) {
        System.out.println(wizardsType + " by " + msg + " exit from \"IvoryTower\"");
    }

    @Override
    public String toString() {
        return "IvoryTower{}";
    }
}

class GeneralTower extends Tower {
    @Override
    public void enterTower(WizardsType wizardsType, String msg) {
        System.out.println(wizardsType + " by " + msg + " enter \"GeneralTower\"");
    }

    @Override
    public void readSpell(WizardsType wizardsType, String msg) {
        System.out.println(wizardsType + " by " + msg + " read spell in \"GeneralTower\"");
    }

    @Override
    public void exitTower(WizardsType wizardsType, String msg) {
        System.out.println(wizardsType + " by " + msg + " exit from \"GeneralTower\"");
    }

    @Override
    public String toString() {
        return "GeneralTower{}";
    }
}

interface BaseTower {
    void enterTower(WizardsType wizardsType, String msg);

    void readSpell(WizardsType wizardsType, String msg);
}

class ImplementBaseTower extends Tower {
    Tower tower;
    WizardsType wizardsType;

    public ImplementBaseTower(Tower tower, WizardsType wizardsType) {
        this.tower = tower;
        this.wizardsType = wizardsType;
    }

    public WizardsType getWizardsType() {
        return wizardsType;
    }

    @Override
    public void enterTower(WizardsType wizardsType, String msg) {
        if (wizardsType.equals(WizardsType.GENERAL) && tower instanceof IvoryTower)
            System.out.println("General Wizard can not enter IvoryTower");
        else {
            tower.enterTower(wizardsType, msg);
        }
    }

    @Override
    public void readSpell(WizardsType wizardsType, String msg) {
        if (wizardsType.equals(WizardsType.GENERAL) && tower instanceof IvoryTower)
            System.out.println("General Wizard can not read spell in IvoryTower");
        else {
            tower.readSpell(wizardsType, msg);
        }
    }

    @Override
    public void exitTower(WizardsType wizardsType, String msg) {
        tower.exitTower(wizardsType, msg);
    }
}

enum WizardsType {
    FIRST_THREE("first three wizard"),
    GENERAL("general wizard");

    WizardsType(String title) {
        this.title = title;
    }

    private final String title;

    @Override
    public String toString() {
        return title;
    }
}

class UseTower {
    static int i;

    public static void main(String[] args) {
        List<ImplementBaseTower> lstWT = new ArrayList<>();
        lstWT.add(new ImplementBaseTower(new IvoryTower(),WizardsType.FIRST_THREE));
        lstWT.add(new ImplementBaseTower(new GeneralTower(), WizardsType.FIRST_THREE));
        lstWT.add(new ImplementBaseTower(new IvoryTower(), WizardsType.GENERAL));
        lstWT.add(new ImplementBaseTower(new GeneralTower(), WizardsType.GENERAL));

        lstWT.stream().forEach(w ->{
            ++i;
            w.enterTower(w.getWizardsType(), "ET" + i);
            w.readSpell(w.getWizardsType(), "WR" + i);
            w.exitTower(w.getWizardsType(), "WE" + i);
        });
    }
}