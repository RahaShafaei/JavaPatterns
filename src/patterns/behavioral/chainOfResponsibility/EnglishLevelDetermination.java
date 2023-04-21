package patterns.behavioral.chainOfResponsibility;

import java.util.*;

enum Level {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED
}

class Applicant {
    String name;
    Level lvl;

    public Applicant(String name, Level lvl) {
        this.lvl = Objects.requireNonNull(lvl);
        this.name = Objects.requireNonNull(name);
    }

    public Level getLvl() {
        return lvl;
    }

    @Override
    public String toString() {
        return "Applicant{" +
                "Name='" + name + '\'' +
                ", Level=" + lvl +
                '}';
    }
}

class Examiner {
    String name;

    public Examiner(String name) {
        this.name = name;
        buildChain();
    }

    private List<LevelDeterminationHandler> handlers;

    private void buildChain() {
        handlers = Arrays.asList(new BeginnerHandler(),
                new IntermediateHandler(),
                new AdvanceHandler());
    }

    public void takeTest(Applicant applicant) {
        handlers
                .stream()
                .filter(handler -> handler.canTestApplicant(applicant))
                .findFirst()
                .ifPresent(handler -> {
                    handler.readingTest(applicant, this);
                    handler.listeningTest(applicant, this);
                    handler.writingTest(applicant, this);
                    handler.speakingTest(applicant, this);
                });
    }

    @Override
    public String toString() {
        return "Examiner{" +
                "name='" + name + '\'' +
                '}';
    }
}

interface LevelDeterminationHandler {
    boolean canTestApplicant(Applicant applicant);

    void readingTest(Applicant applicant, Examiner examiner);

    void listeningTest(Applicant applicant, Examiner examiner);

    void speakingTest(Applicant applicant, Examiner examiner);

    void writingTest(Applicant applicant, Examiner examiner);
}

class BeginnerHandler implements LevelDeterminationHandler {
    @Override
    public boolean canTestApplicant(Applicant applicant) {
        return applicant.getLvl() == Level.BEGINNER;
    }

    @Override
    public void readingTest(Applicant applicant, Examiner examiner) {
        System.out.println("Beginner reading tests were done for : " + applicant + " by : " + examiner);
    }

    @Override
    public void listeningTest(Applicant applicant, Examiner examiner) {
        System.out.println("Beginner listening tests were done for : " + applicant + " by : " + examiner);

    }

    @Override
    public void speakingTest(Applicant applicant, Examiner examiner) {
        System.out.println("Beginner speaking tests were done for : " + applicant + " by : " + examiner);
    }

    @Override
    public void writingTest(Applicant applicant, Examiner examiner) {
        System.out.println("Beginner writing tests were done for : " + applicant + " by : " + examiner);
    }
}

class IntermediateHandler implements LevelDeterminationHandler {
    @Override
    public boolean canTestApplicant(Applicant applicant) {
        return applicant.getLvl() == Level.INTERMEDIATE;
    }

    @Override
    public void readingTest(Applicant applicant, Examiner examiner) {
        System.out.println("Intermediate reading tests were done for : " + applicant + " by : " + examiner);
    }

    @Override
    public void listeningTest(Applicant applicant, Examiner examiner) {
        System.out.println("Intermediate listening tests were done for : " + applicant + " by : " + examiner);

    }

    @Override
    public void speakingTest(Applicant applicant, Examiner examiner) {
        System.out.println("Intermediate speaking tests were done for : " + applicant + " by : " + examiner);
    }

    @Override
    public void writingTest(Applicant applicant, Examiner examiner) {
        System.out.println("Intermediate writing tests were done for : " + applicant + " by : " + examiner);
    }
}

class AdvanceHandler implements LevelDeterminationHandler {
    @Override
    public boolean canTestApplicant(Applicant applicant) {
        return applicant.getLvl() == Level.ADVANCED;
    }

    @Override
    public void readingTest(Applicant applicant, Examiner examiner) {
        System.out.println("Advance reading tests were done for : " + applicant + " by : " + examiner);
    }

    @Override
    public void listeningTest(Applicant applicant, Examiner examiner) {
        System.out.println("Advance listening tests were done for : " + applicant + " by : " + examiner);

    }

    @Override
    public void speakingTest(Applicant applicant, Examiner examiner) {
        System.out.println("Advance speaking tests were done for : " + applicant + " by : " + examiner);
    }

    @Override
    public void writingTest(Applicant applicant, Examiner examiner) {
        System.out.println("Advance writing tests were done for : " + applicant + " by : " + examiner);
    }
}

public class EnglishLevelDetermination {
    public static void main(String[] args) {
        Examiner examiner = new Examiner("The regular tester");
        examiner.takeTest(new Applicant("App1",Level.BEGINNER));
        examiner.takeTest(new Applicant("App2",Level.INTERMEDIATE));
        examiner.takeTest(new Applicant("App3",Level.BEGINNER));
        System.out.println(":::::::::::::::::::::::::::::::::::::::::::");
        Examiner mainExaminer = new Examiner("The main tester");
        mainExaminer.takeTest(new Applicant("Adv App",Level.ADVANCED));
    }
}
