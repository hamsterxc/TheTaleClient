package com.lonebytesoft.thetaleclient.sdk.model;

import com.lonebytesoft.thetaleclient.sdk.dictionary.ArtifactEffect;
import com.lonebytesoft.thetaleclient.sdk.dictionary.ArtifactRarity;
import com.lonebytesoft.thetaleclient.sdk.dictionary.ArtifactType;
import com.lonebytesoft.thetaleclient.sdk.util.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 15.03.2015
 */
public class ArtifactInfo {

    public final int id;
    public final String name;
    public final int powerPhysical;
    public final int powerMagical;
    public final ArtifactType type;
    public final int integrityCurrent;
    public final int integrityTotal;
    public final ArtifactRarity rarity;
    public final ArtifactEffect effect;
    public final ArtifactEffect effectSpecial;
    public final double rating;
    public final boolean isEquippable;

    public ArtifactInfo(final JSONObject json) throws JSONException {
        name = json.getString("name");
        type = ObjectUtils.getEnumForCode(ArtifactType.class, json.getInt("type"));
        if(type == ArtifactType.JUNK) {
            powerPhysical = 0;
            powerMagical = 0;
            integrityCurrent = 0;
            integrityTotal = 0;
            rarity = ArtifactRarity.COMMON;
            rating = 0;
        } else {
            powerPhysical = json.getJSONArray("power").getInt(0);
            powerMagical = json.getJSONArray("power").getInt(1);
            integrityCurrent = json.getJSONArray("integrity").getInt(0);
            integrityTotal = json.getJSONArray("integrity").getInt(1);
            rarity = ObjectUtils.getEnumForCode(ArtifactRarity.class, json.getInt("rarity"));
            rating = json.getDouble("preference_rating");
        }
        effect = ObjectUtils.getEnumForCode(ArtifactEffect.class, json.getInt("effect"));
        effectSpecial = ObjectUtils.getEnumForCode(ArtifactEffect.class, json.getInt("special_effect"));
        isEquippable = json.getBoolean("equipped");
        id = json.getInt("id");
    }

}
