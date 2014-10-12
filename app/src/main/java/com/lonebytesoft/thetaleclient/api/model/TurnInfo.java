package com.lonebytesoft.thetaleclient.api.model;

/**
 * @author Hamster
 * @since 01.10.2014
 */
public class TurnInfo {

    public final int turnNumber;
    public final String verboseDate;
    public final String verboseTime;

    public TurnInfo(final int turnNumber, final String verboseDate, final String verboseTime) {
        this.turnNumber = turnNumber;
        this.verboseDate = verboseDate;
        this.verboseTime = verboseTime;
    }

}
