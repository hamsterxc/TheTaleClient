package com.lonebytesoft.thetaleclient.sdk.model;

import com.lonebytesoft.thetaleclient.sdk.dictionary.SocialLink;
import com.lonebytesoft.thetaleclient.sdk.util.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 06.05.2015
 */
public class CouncilMemberConnectionInfo {

    public final SocialLink type;
    public final int councilMemberId;

    public CouncilMemberConnectionInfo(final JSONObject json) throws JSONException {
        type = ObjectUtils.getEnumForCode(SocialLink.class, json.getInt("social_link"));
        councilMemberId = json.getInt("council_member");
    }

}
