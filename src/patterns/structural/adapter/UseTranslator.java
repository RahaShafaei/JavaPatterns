package patterns.structural.adapter;

enum Language{
    ENGLISH,
    PERSIAN,
    SPANISH
}

interface Translate{
    void doTranslate(Language currentLng, Language anotherLng);
}

class Translator implements Translate{
    SpokenLngConvertor spokenLng;

    public Translator(SpokenLngConvertor spokenLng) {
        this.spokenLng = spokenLng;
    }

    @Override
    public void doTranslate(Language currentLng, Language anotherLng) {
        spokenLng.textConvertor(currentLng,anotherLng);
        spokenLng.voiceConvertor(currentLng,anotherLng);
    }
}

class SpokenLngConvertor{

    void textConvertor(Language currentLng, Language anotherLng) {
        System.out.println("Convert " + currentLng + " TEXT to " + anotherLng);
    }

    void voiceConvertor(Language currentLng, Language anotherLng){
        System.out.println("Convert " + currentLng + " VOICE to " + anotherLng);

    }

}

class UsingTranslator {

    void translating(Translate translate, Language curr, Language anoth){
        translate.doTranslate(curr,anoth);
    }
}

public class UseTranslator {
    public static void main(String[] args) {
        SpokenLngConvertor spokenLng = new SpokenLngConvertor();
        Translate translate = new Translator(spokenLng);
        UsingTranslator usingTranslator = new UsingTranslator();

        usingTranslator.translating(translate,Language.ENGLISH,Language.PERSIAN);
        usingTranslator.translating(translate,Language.PERSIAN,Language.ENGLISH);
        usingTranslator.translating(translate,Language.ENGLISH,Language.SPANISH);
    }
}
