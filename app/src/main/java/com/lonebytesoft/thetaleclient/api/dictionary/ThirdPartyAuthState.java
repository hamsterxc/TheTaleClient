package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 29.10.2014
 */
public enum ThirdPartyAuthState {

    NOT_REQUESTED(0, "Авторизация не запрашивалась"),
    NOT_DECIDED(1, "Пользователь ещё не принял решение"),
    SUCCESS(2, "Авторизация прошла успешно"),
    REJECT(3, "В авторизации отказано"),
    ;

    private final int code;
    private final String description;

    private ThirdPartyAuthState(final int code, final String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}
