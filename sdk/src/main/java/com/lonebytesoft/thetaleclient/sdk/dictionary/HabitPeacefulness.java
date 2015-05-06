package com.lonebytesoft.thetaleclient.sdk.dictionary;

/**
 * @author Hamster
 * @since 06.05.2015
 */
public enum HabitPeacefulness {

    VIOLENT(0, "скорый на расправу", "территория вендетт"),
    IRASCIBLE(1, "вспыльчивый", "пристанище горячих голов"),
    BULLY(2, "задира", "беспокойное место"),
    MODERATE(3, "сдержанный", "неприметное поселение"),
    KIND(4, "доброхот", "спокойное место"),
    PACIFIC(5, "миролюбивый", "мирное поселение"),
    HUMANIST(6, "гуманист", "центр цивилизации"),
    ;

    public final int code;
    public final String name;
    public final String nameHero;
    public final String namePlace;

    HabitPeacefulness(final int code, final String nameHero, final String namePlace) {
        this.code = code;
        this.name = nameHero;
        this.nameHero = nameHero;
        this.namePlace = namePlace;
    }

}
