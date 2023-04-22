package patterns.Creational.observer;

import java.util.ArrayList;
import java.util.List;

enum SongType {
    SAD,
    HAPPY,
    SILENT
}

abstract class Musician {
    SongType state;

    public abstract void setState(SongType state);

    public void playing(){
        switch (this.state){
            case SILENT -> this.silent();
            case SAD -> this.playQuietly();
            case HAPPY ->this.playLoudly();
        }
    }

    public abstract void playLoudly();

    public abstract void playQuietly();

    public abstract void silent();
}

class Guitarist extends Musician {
    @Override
    public void setState(SongType state) {
        super.state = state;
    }

    @Override
    public void playLoudly() {
        System.out.println("Song type is " +super.state + " and Guitarist play loudly.");
    }

    @Override
    public void playQuietly() {
        System.out.println("Song type is " +super.state + " and Guitarist play quietly.");
    }

    @Override
    public void silent() {
        System.out.println("Song type is " +super.state + " and Guitarist do nothing.");
    }
}

class Pianist extends Musician {
    @Override
    public void setState(SongType state) {
        super.state = state;
    }

    @Override
    public void playLoudly() {
        System.out.println("Song type is " +super.state + " and Pianist play loudly.");
    }

    @Override
    public void playQuietly() {
        System.out.println("Song type is " +super.state + " and Pianist play quietly.");
    }

    @Override
    public void silent() {
        System.out.println("Song type is " +super.state + " and Pianist do nothing.");
    }
}

class Conductor {
    private SongType songType;
    final List<Musician> musicians;

    public Conductor() {
        this.songType = SongType.SILENT;
        musicians = new ArrayList<>();
    }

    public void addMusician(Musician musician) {
        musicians.add(musician);
    }

    public void removeMusician(Musician musician) {
        musicians.remove(musician);
    }


    void conducting(SongType songType) {
        this.songType = songType;
        this.notifyMusicians();
    }

    private void notifyMusicians() {
        for (var mus : musicians) {
            mus.setState(this.songType);
            mus.playing();
        }
    }
}

public class Concert {
    public static void main(String[] args) {
        Conductor conductor = new Conductor();

        conductor.addMusician(new Pianist());
        conductor.addMusician(new Guitarist());

        conductor.conducting(SongType.SILENT);
        conductor.conducting(SongType.SAD);
        conductor.conducting(SongType.HAPPY);
        conductor.conducting(SongType.SILENT);
    }
}
