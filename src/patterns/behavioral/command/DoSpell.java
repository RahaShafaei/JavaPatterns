package patterns.behavioral.command;

enum Type {
    SHRINK,
    ENLARGE,
    INVISIBLE
}

class Goblin {
    String name;

    public Goblin(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Goblin{" +
                "name='" + name + '\'' +
                '}';
    }
}

class Wizard {
    Goblin goblin;

    public Wizard(Goblin goblin) {
        this.goblin = goblin;
    }

    public void castingSpell( Spell spell) {
        spell.executeOn(this.goblin);
    }
}

class Spell {
    Type type;

    public Spell(Type type) {
        this.type = type;
    }

    void executeOn(Goblin goblin) {
        switch (type) {
            case SHRINK:
                shrinking(goblin);
                break;
            case ENLARGE:
                enlarging(goblin);
                break;
            case INVISIBLE:
                invisibling(goblin);
                break;
        }
    }

    void shrinking(Goblin goblin) {
        System.out.println("Shrinking " + goblin);
    }

    void enlarging(Goblin goblin) {
        System.out.println("Enlarging " + goblin);
    }

    void invisibling(Goblin goblin) {
        System.out.println("Invisibling " + goblin);
    }
}

public class DoSpell {
    public static void main(String[] args) {
        Wizard wizard = new Wizard(new Goblin("Master"));
        wizard.castingSpell(new Spell(Type.ENLARGE));
        wizard.castingSpell(new Spell(Type.SHRINK));
        wizard.castingSpell(new Spell(Type.INVISIBLE));
    }
}

