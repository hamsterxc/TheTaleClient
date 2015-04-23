package com.lonebytesoft.thetaleclient.apisdk.util;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.sdk.dictionary.ArtifactRarity;
import com.lonebytesoft.thetaleclient.sdk.dictionary.EquipmentType;
import com.lonebytesoft.thetaleclient.sdk.dictionary.QuestActorType;
import com.lonebytesoft.thetaleclient.sdk.dictionary.QuestType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hamster
 * @since 23.04.2015
 */
public class DictionaryData {

    private static final Map<QuestType, Integer> questTypeDrawableId;
    private static final Map<ArtifactRarity, Integer> artifactRarityColorId;
    private static final Map<QuestActorType, Integer> questActorTypeStringId;
    private static final Map<EquipmentType, Integer> equipmentTypeDrawableId;

    public static int getQuestTypeDrawableId(final QuestType questType) {
        return questTypeDrawableId.get(questType);
    }

    public static int getArtifactRarityColorId(final ArtifactRarity artifactRarity) {
        return artifactRarityColorId.get(artifactRarity);
    }

    public static int getQuestActorTypeStringId(final QuestActorType questActorType) {
        return questActorTypeStringId.get(questActorType);
    }

    public static int getEquipmentTypeDrawableId(final EquipmentType equipmentType) {
        return equipmentTypeDrawableId.get(equipmentType);
    }

    static {
        questTypeDrawableId = new HashMap<QuestType, Integer>(QuestType.values().length) {{
            put(QuestType.CARAVAN, R.drawable.quest_caravan);
            put(QuestType.DELIVERY, R.drawable.quest_delivery);
            put(QuestType.HELP, R.drawable.quest_help);
            put(QuestType.HELP_FRIEND, R.drawable.quest_help_friend);
            put(QuestType.HOMETOWN, R.drawable.quest_hometown);
            put(QuestType.HUNT, R.drawable.quest_hunt);
            put(QuestType.ENEMY, R.drawable.quest_enemy);
            put(QuestType.DEBT, R.drawable.quest_debt);
            put(QuestType.SPYING, R.drawable.quest_spying);
            put(QuestType.SMITH, R.drawable.quest_smith);
            put(QuestType.NO_QUEST, R.drawable.quest_no_quest);
            put(QuestType.SPENDING, R.drawable.quest_next_spending);
            put(QuestType.PILGRIMAGE, R.drawable.quest_pilgrimage);
        }};

        artifactRarityColorId = new HashMap<ArtifactRarity, Integer>(ArtifactRarity.values().length) {{
            put(ArtifactRarity.COMMON, R.color.artifact_common);
            put(ArtifactRarity.RARE, R.color.artifact_rare);
            put(ArtifactRarity.EPIC, R.color.artifact_epic);
        }};

        questActorTypeStringId = new HashMap<QuestActorType, Integer>(QuestActorType.values().length) {{
            put(QuestActorType.PLACE, R.string.quest_actor_type_place);
            put(QuestActorType.PERSON, R.string.quest_actor_type_person);
            put(QuestActorType.SPENDING, R.string.quest_actor_type_spending);
        }};

        equipmentTypeDrawableId = new HashMap<EquipmentType, Integer>(EquipmentType.values().length) {{
            put(EquipmentType.MAIN_HAND, R.drawable.artifact_main_hand);
            put(EquipmentType.OFF_HAND, R.drawable.artifact_off_hand);
            put(EquipmentType.HEAD, R.drawable.artifact_head);
            put(EquipmentType.AMULET, R.drawable.artifact_amulet);
            put(EquipmentType.SHOULDERS, R.drawable.artifact_shoulders);
            put(EquipmentType.BODY, R.drawable.artifact_body);
            put(EquipmentType.GLOVES, R.drawable.artifact_gloves);
            put(EquipmentType.CLOAK, R.drawable.artifact_cloak);
            put(EquipmentType.TROUSERS, R.drawable.artifact_trousers);
            put(EquipmentType.BOOTS, R.drawable.artifact_boots);
            put(EquipmentType.RING, R.drawable.artifact_ring);
        }};
    }

}
