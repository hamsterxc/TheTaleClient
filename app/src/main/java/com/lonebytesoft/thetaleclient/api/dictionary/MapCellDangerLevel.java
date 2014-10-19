package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 17.10.2014
 */
public enum MapCellDangerLevel {

    // codes must be lowercase (for parsing)
    AVERAGE("местность выглядит настораживающе, расслабляться не стоит — опасность рядом"),
    HIGH("пейзаж выглядит устрашающе, присмотревшись, можно увидеть свежие следы хищных тварей"),
    VERY_HIGH("хищные взгляды множества голодных тварей не дадут расслабится ни одному смельчаку, рискнувшему забраться в эту глушь"),
    ;

    private final String code;

    private MapCellDangerLevel(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
