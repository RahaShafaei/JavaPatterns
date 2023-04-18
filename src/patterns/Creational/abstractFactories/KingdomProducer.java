package patterns.Creational.abstractFactories;

import java.util.function.Supplier;


interface King {
    void reign();
}

interface Castle {
    String wall = "";
    String battlement = "";
    String tower = "";
    String moat = "";

    void protection();
}

interface Army {
    void battle();
}

class ElvenKing implements King {

    @Override
    public void reign() {
        System.out.println("Elven king reign!");
    }
}

class OrcishKing implements King {

    @Override
    public void reign() {
        System.out.println("Orcish king reign!");
    }
}

class ElvenCastle implements Castle {

    @Override
    public void protection() {
        System.out.println("Elven castle protection!");
    }
}

class OrcishCastle implements Castle {

    @Override
    public void protection() {
        System.out.println("Orcish castle protection!");

    }
}

class ElvenArmy implements Army {

    @Override
    public void battle() {
        System.out.println("Elven army battle!");
    }
}

class OrcishArmy implements Army {

    @Override
    public void battle() {
        System.out.println("Orcish army battle!");
    }
}

class KingdomPartsFactory {
    Supplier<King> king;
    Supplier<Castle> castle;
    Supplier<Army> army;
}

class ElvenKingdom extends KingdomPartsFactory {
    ElvenKingdom() {
        king = ElvenKing::new;
        castle = ElvenCastle::new;
        army = ElvenArmy::new;
    }
}

class OrcishKingdom extends KingdomPartsFactory {
    OrcishKingdom() {
        king = OrcishKing::new;
        castle = OrcishCastle::new;
        army = OrcishArmy::new;
    }
}

class WarPlace {
    King king;
    Castle castle;
    Army army;

    public WarPlace(KingdomPartsFactory kf) {
        this.king = kf.king.get();
        this.castle = kf.castle.get();
        this.army = kf.army.get();
    }

    public void war() {
        king.reign();
        castle.protection();
        army.battle();
    }
}

public class KingdomProducer {
    public static void main(String[] args) {
        KingdomPartsFactory
                ek = new ElvenKingdom(),
                ok = new OrcishKingdom();
        WarPlace ew = new WarPlace(ek),
                ow = new WarPlace(ok);
        ew.war();
        ow.war();
    }

}
