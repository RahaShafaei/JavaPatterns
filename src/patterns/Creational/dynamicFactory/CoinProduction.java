package patterns.Creational.dynamicFactory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class BadShapeCreation extends RuntimeException {
    public BadShapeCreation(String msg) {
        super(msg);
    }
}

class Coin {
    public Coin(Helper helper) {
        this.helper = helper;
    }

    Helper helper;

    public Helper getHelper() {
        return helper;
    }

    public void setHelper(Helper helper) {
        this.helper = helper;
    }

    void produce() {
        System.out.println(helper.getCoinType() + ": Produce "
                + this.helper.getNum() + " numbers of "
                + this.getClass().getSimpleName() + " coins!");
    }

    void engrave() {
        System.out.println(helper.getCoinType() + ": Engrave "
                + this.helper.getNum() + " numbers of "
                + this.getClass().getSimpleName() + " coins!");
    }
}

class Golden extends Coin {
    public Golden(Helper helper) {
        super(helper);
    }
}

class Silver extends Coin {
    public Silver(Helper helper) {
        super(helper);
    }
}

class Copper extends Coin {
    public Copper(Helper helper) {
        super(helper);
    }
}

class Helper {
    Integer num;
    String CoinType;

    public Helper(Integer num, String coinType) {
        this.num = num;
        CoinType = coinType;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getCoinType() {
        return CoinType;
    }

    public void setCoinType(String coinType) {
        CoinType = coinType;
    }
}

interface Alchemist {
    Coin create(Helper helper);
}

class OrderCoins {
    public static void orderTo(Alchemist alchemist) {
        List<Helper> lst = new ArrayList<>();

        lst.add(new Helper(3, "Golden"));
        lst.add(new Helper(5, "Silver"));
        lst.add(new Helper(10, "Copper"));

        lst.stream()
                .map(alchemist::create)
                .forEach(a -> {
                    a.produce();
                    a.engrave();
                });
    }
}

public class CoinProduction implements Alchemist {

    public static void main(String[] args) {
        OrderCoins.orderTo(new CoinProduction());
    }

    private Map<Helper, Constructor> factories = new HashMap<>();

    private static Constructor load(Helper helper) {
        System.out.println("loading " + helper.getCoinType());
        try {
            return Class.forName("patterns.Creational.dynamicFactory." + helper.getCoinType())
                    .getConstructor(Helper.class);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            throw new BadShapeCreation(e.getMessage());
        }
    }

    @Override
    public Coin create(Helper helper) {
        try {
            System.out.println("ccccccccccccccccccc");
            return  (Coin) factories
                    .computeIfAbsent(helper, CoinProduction::load)
                    .newInstance(helper);
        } catch (Exception e) {
            throw new BadShapeCreation(e.getMessage());
        }
    }

}
