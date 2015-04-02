package com.lonebytesoft.thetaleclient.sdk.dictionary;

/**
 * @author Hamster
 * @since 18.03.2015
 */
public enum Profession {

    SMITH(0, "кузнец"),
    FISHER(1, "рыбак"),
    TAILOR(2, "портной"),
    CARPENTER(3, "плотник"),
    HUNTER(4, "охотник"),
    GUARD(5, "стражник"),
    TRADER(6, "торговец"),
    INNKEEPER(7, "трактирщик"),
    THIEF(8, "вор"),
    FARMER(9, "фермер"),
    MINER(10, "шахтёр"),
    PRIEST(11, "священник"),
    HEALER(12, "лекарь"),
    ALCHEMIST(13, "алхимик"),
    EXECUTIONER(14, "палач"),
    WIZARD(15, "волшебник"),
    MAYOR(16, "мэр"),
    BUREAUCRAT(17, "бюрократ"),
    ARISTOCRAT(18, "аристократ"),
    BARD(19, "бард"),
    TRAINER(20, "дрессировщик"),
    HERDER(21, "скотовод"),
    ;

    public final int code;
    public final String name;

    Profession(final int code, final String name) {
        this.code = code;
        this.name = name;
    }

}
