package com.lonebytesoft.thetaleclient.api.model;

import com.lonebytesoft.thetaleclient.api.dictionary.ArtifactEffect;
import com.lonebytesoft.thetaleclient.api.dictionary.ArtifactOrientation;
import com.lonebytesoft.thetaleclient.api.dictionary.ArtifactType;
import com.lonebytesoft.thetaleclient.util.ObjectUtils;

/**
 * @author Hamster
 * @since 25.01.2015
 */
public class ArtifactBaseInfo {

    public final String name;
    public final int level;
    public final ArtifactType type;
    public final ArtifactOrientation orientation;
    public final ArtifactEffect effectRare;
    public final ArtifactEffect effectEpic;
    public final ArtifactEffect effectSpecial;
    public final String description;

    public ArtifactBaseInfo(final String name, final String level, final String type, final String orientation,
                            final String effectRare, final String effectEpic, final String effectSpecial,
                            final String description) {
        this.name = name;
        this.level = Integer.parseInt(level);
        this.type = ObjectUtils.getEnumForName(ArtifactType.class, type);
        this.orientation = ObjectUtils.getEnumForCode(ArtifactOrientation.class, orientation);
        this.effectRare = getArtifactEffectForName(effectRare);
        this.effectEpic = getArtifactEffectForName(effectEpic);
        this.effectSpecial = getArtifactEffectForName(effectSpecial);
        this.description = description;
    }

    private ArtifactEffect getArtifactEffectForName(final String name) {
        final ArtifactEffect artifactEffect = ObjectUtils.getEnumForName(ArtifactEffect.class, name);
        return artifactEffect == null ? ArtifactEffect.NO_EFFECT : artifactEffect;
    }

}
