package com.lonebytesoft.thetaleclient.api.dictionary;

import java.text.DecimalFormat;

/**
 * @author Hamster
 * @since 20.02.2015
 */
public enum RatingItem {

    MIGHT("might") {
        @Override
        public String getValue(final double value) {
            return new DecimalFormat("#.##").format(value);
        }
    },
    BILLS("bills"),
    MAGIC_POWER("magic-power"),
    PHYSICAL_POWER("physic-power"),
    LEVEL("level"),
    PHRASES("phrases"),
    PVP_COUNT("pvp_battles_1x1_number"),
    PVP_VICTORIES("pvp_battles_1x1_victories") {
        @Override
        public String getValue(final double value) {
            return String.format("%.2f%%", value * 100);
        }
    },
    REFERRALS("referrals_number"),
    ACHIEVEMENT_POINTS("achievements_points"),
    HELP("help_count"),
    GIFTS_RETURNED("gifts_returned"),
    ;

    private final String code;

    private RatingItem(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getValue(final double value) {
        return String.valueOf((int) Math.floor(value));
    }

}
