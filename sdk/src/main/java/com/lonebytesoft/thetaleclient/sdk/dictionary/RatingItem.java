package com.lonebytesoft.thetaleclient.sdk.dictionary;

/**
 * @author Hamster
 * @since 23.03.2015
 */
public enum RatingItem {

    MIGHT("might"),
    BILLS("bills"),
    MAGIC_POWER("magic-power"),
    PHYSICAL_POWER("physic-power"),
    LEVEL("level"),
    PHRASES("phrases"),
    PVP_COUNT("pvp_battles_1x1_number"),
    PVP_VICTORIES("pvp_battles_1x1_victories"),
    REFERRALS("referrals_number"),
    ACHIEVEMENT_POINTS("achievements_points"),
    HELP("help_count"),
    GIFTS_RETURNED("gifts_returned"),
    ;

    public final String code;
    public final String name;

    RatingItem(final String code) {
        this.code = code;
        this.name = code;
    }

}
