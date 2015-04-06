package com.lonebytesoft.thetaleclient.sdk.dictionary;

/**
 * @author Hamster
 * @since 03.04.2015
 */
public enum ThirdPartyAuthState {

    NOT_REQUESTED(0, "авторизация не запрашивалась"),
    NOT_DECIDED(1, "пользователь ещё не принял решение"),
    SUCCESS(2, "авторизация прошла успешно"),
    REJECT(3, "в авторизации отказано"),
    ;

    public final int code;
    public final String name;

    ThirdPartyAuthState(final int code, final String name) {
        this.code = code;
        this.name = name;
    }

}
