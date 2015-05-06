package com.lonebytesoft.thetaleclient.sdk.dictionary;

/**
 * @author Hamster
 * @since 06.05.2015
 */
public enum HabitHonor {

    DISHONORABLE(0, "бесчестный", "криминальная столица"),
    VILE(1, "подлый", "бандитская вотчина"),
    VICIOUS(2, "порочный", "неблагополучный город"),
    CANNY(3, "себе на уме", "обычный город"),
    DECENT(4, "порядочный", "благополучное поселение"),
    FAIR(5, "благородный", "честный город"),
    HONORABLE(6, "хозяин своего слова", "оплот благородства"),
    ;

    public final int code;
    public final String name;
    public final String nameHero;
    public final String namePlace;

    HabitHonor(final int code, final String nameHero, final String namePlace) {
        this.code = code;
        this.name = nameHero;
        this.nameHero = nameHero;
        this.namePlace = namePlace;
    }

}
