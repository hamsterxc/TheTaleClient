package com.lonebytesoft.thetaleclient.sdk.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 21.05.2015
 */
public class AccountShortInfo {

    public final int id;
    public final String name;
    public final double might;
    public final long timeRegistered; // UNIX time, seconds

    public AccountShortInfo(final JSONObject json) throws JSONException {
        id = json.getInt("id");
        name = json.getString("name");
        might = json.getDouble("might");
        timeRegistered = json.getLong("time_registered");
    }

    public AccountShortInfo(final String id, final String name,
                            final String might, final String timeRegistered) {
        try {
            this.id = Integer.decode(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("Invalid id: %s - %s", id, e.getMessage()));
        }

        this.name = name;

        try {
            this.might = Double.parseDouble(might);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("Invalid might: %s - %s", might, e.getMessage()));
        }

        try {
            this.timeRegistered = (long) Double.parseDouble(timeRegistered);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("Invalid time registered: %s - %s",
                    timeRegistered, e.getMessage()));
        }
    }

}
