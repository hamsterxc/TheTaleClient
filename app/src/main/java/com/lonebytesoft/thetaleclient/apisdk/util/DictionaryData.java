package com.lonebytesoft.thetaleclient.apisdk.util;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.sdk.dictionary.ArtifactRarity;
import com.lonebytesoft.thetaleclient.sdk.dictionary.CardRarity;
import com.lonebytesoft.thetaleclient.sdk.dictionary.EquipmentType;
import com.lonebytesoft.thetaleclient.sdk.dictionary.Profession;
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
    private static final Map<CardRarity, Integer> cardRarityColorId;
    private static final Map<Profession, Integer> professionDrawableId;

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

    public static int getCardRarityColorId(final CardRarity cardRarity) {
        return cardRarityColorId.get(cardRarity);
    }

    public static int getProfessionDrawableId(final Profession profession) {
        return professionDrawableId.get(profession);
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

        cardRarityColorId = new HashMap<CardRarity, Integer>(CardRarity.values().length) {{
            put(CardRarity.COMMON, R.color.card_common);
            put(CardRarity.UNCOMMON, R.color.card_uncommon);
            put(CardRarity.RARE, R.color.card_rare);
            put(CardRarity.EPIC, R.color.card_epic);
            put(CardRarity.LEGENDARY, R.color.card_legendary);
        }};

        professionDrawableId = new HashMap<Profession, Integer>(Profession.values().length) {{
            put(Profession.SMITH, R.drawable.profession_blacksmith);
            put(Profession.FISHER, R.drawable.profession_fisherman);
            put(Profession.TAILOR, R.drawable.profession_tailor);
            put(Profession.CARPENTER, R.drawable.profession_carpenter);
            put(Profession.HUNTER, R.drawable.profession_hunter);
            put(Profession.GUARD, R.drawable.profession_warden);
            put(Profession.TRADER, R.drawable.profession_merchant);
            put(Profession.INNKEEPER, R.drawable.profession_innkeeper);
            put(Profession.THIEF, R.drawable.profession_rogue);
            put(Profession.FARMER, R.drawable.profession_farmer);
            put(Profession.MINER, R.drawable.profession_miner);
            put(Profession.PRIEST, R.drawable.profession_priest);
            put(Profession.HEALER, R.drawable.profession_physician);
            put(Profession.ALCHEMIST, R.drawable.profession_alchemist);
            put(Profession.EXECUTIONER, R.drawable.profession_executioner);
            put(Profession.WIZARD, R.drawable.profession_magician);
            put(Profession.MAYOR, R.drawable.profession_mayor);
            put(Profession.BUREAUCRAT, R.drawable.profession_bureaucrat);
            put(Profession.ARISTOCRAT, R.drawable.profession_aristocrat);
            put(Profession.BARD, R.drawable.profession_bard);
            put(Profession.TRAINER, R.drawable.profession_tamer);
            put(Profession.HERDER, R.drawable.profession_herdsman);
        }};
    }

}
