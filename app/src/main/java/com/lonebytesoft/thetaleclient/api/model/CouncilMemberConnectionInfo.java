package com.lonebytesoft.thetaleclient.api.model;

import com.lonebytesoft.thetaleclient.api.dictionary.SocialLink;
import com.lonebytesoft.thetaleclient.util.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 05.05.2015
 */
public class CouncilMemberConnectionInfo {

    public SocialLink type;
    public int councilMemberId;

    public CouncilMemberConnectionInfo(final JSONObject json) throws JSONException {
        type = ObjectUtils.getEnumForCode(SocialLink.class, json.getInt("social_link"));
        councilMemberId = json.getInt("council_member");
    }

}
