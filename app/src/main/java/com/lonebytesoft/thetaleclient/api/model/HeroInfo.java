package com.lonebytesoft.thetaleclient.api.model;

import com.lonebytesoft.thetaleclient.api.dictionary.EquipmentType;
import com.lonebytesoft.thetaleclient.api.dictionary.Habit;
import com.lonebytesoft.thetaleclient.util.ObjectUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Hamster
 * @since 02.10.2014
 */
public class HeroInfo {

    public final int id;
    public final int actualityTurnNumber;
    public final int spriteId;
    public final PvpHeroInfo pvpInfo;
    public final EnergyInfo energy;
    public final Map<EquipmentType, ArtifactInfo> equipment;
    public final CardsInfo cards;
    public final Map<Integer, ArtifactInfo> bag;
    public final HeroBasicInfo basicInfo;
    public final List<DiaryEntry> diary;
    public final List<JournalEntry> journal;
    public final Map<Habit, HabitInfo> habits;
    public final List<List<QuestStepInfo>> quests;
    public final HeroActionInfo action;
    public final PositionInfo position;
    public final boolean canPvp;
    public final boolean canRepair;
    public final MightInfo might;
    public final CompanionInfo companionInfo;

    public HeroInfo(final JSONObject json) throws JSONException {
        pvpInfo = ObjectUtils.getModelFromJson(PvpHeroInfo.class, json.getJSONObject("pvp"));

        energy = ObjectUtils.getModelFromJson(EnergyInfo.class, json.getJSONObject("energy"));

        equipment = new HashMap<>(EquipmentType.values().length);
        final JSONObject equipmentJson = json.getJSONObject("equipment");
        for(final EquipmentType equipmentType : EquipmentType.values()) {
            final String key = String.valueOf(equipmentType.getCode());
            if(equipmentJson.has(key)) {
                equipment.put(equipmentType, ObjectUtils.getModelFromJson(ArtifactInfo.class, equipmentJson.getJSONObject(key)));
            }
        }

        cards = ObjectUtils.getModelFromJson(CardsInfo.class, json.getJSONObject("cards"));

        bag = new HashMap<>();
        final JSONObject bagJson = json.getJSONObject("bag");
        for(final Iterator<String> bagJsonIterator = bagJson.keys(); bagJsonIterator.hasNext();) {
            final String key = bagJsonIterator.next();
            bag.put(Integer.decode(key), ObjectUtils.getModelFromJson(ArtifactInfo.class, bagJson.getJSONObject(key)));
        }

        final JSONObject heroBasicInfoJson = new JSONObject();
        final JSONObject primaryHeroInfoJson = json.getJSONObject("base");
        for(final Iterator<String> primaryHeroInfoIterator = primaryHeroInfoJson.keys(); primaryHeroInfoIterator.hasNext();) {
            final String key = primaryHeroInfoIterator.next();
            heroBasicInfoJson.put(key, primaryHeroInfoJson.get(key));
        }
        final JSONObject secondaryHeroInfoJson = json.getJSONObject("secondary");
        for(final Iterator<String> secondaryHeroInfoIterator = secondaryHeroInfoJson.keys(); secondaryHeroInfoIterator.hasNext();) {
            final String key = secondaryHeroInfoIterator.next();
            heroBasicInfoJson.put(key, secondaryHeroInfoJson.get(key));
        }
        basicInfo = ObjectUtils.getModelFromJson(HeroBasicInfo.class, heroBasicInfoJson);

        final JSONArray diaryJson = json.getJSONArray("diary");
        final int diaryEntriesCount = diaryJson.length();
        diary = new ArrayList<>(diaryEntriesCount);
        for(int i = 0; i < diaryEntriesCount; i++) {
            diary.add(ObjectUtils.getModelFromJson(DiaryEntry.class,
                    ObjectUtils.getObjectFromArray(diaryJson.getJSONArray(i), new String[]{"timestamp", "time", "text", "date"})));
        }

        final JSONArray journalJson = json.getJSONArray("messages");
        final int journalEntriesCount = journalJson.length();
        journal = new ArrayList<>(journalEntriesCount);
        for(int i = 0; i < journalEntriesCount; i++) {
            journal.add(ObjectUtils.getModelFromJson(JournalEntry.class,
                    ObjectUtils.getObjectFromArray(journalJson.getJSONArray(i), new String[]{"timestamp", "time", "text"})));
        }

        habits = new HashMap<>(Habit.values().length);
        final JSONObject habitsJson = json.getJSONObject("habits");
        for(final Habit habit : Habit.values()) {
            habits.put(habit, ObjectUtils.getModelFromJson(HabitInfo.class, habitsJson.getJSONObject(habit.getCode())));
        }

        final JSONArray questsJson = json.getJSONObject("quests").getJSONArray("quests");
        final int questsCount = questsJson.length();
        quests = new ArrayList<>(questsCount);
        for(int i = 0; i < questsCount; i++) {
            final JSONArray questStepsJson = questsJson.getJSONObject(i).getJSONArray("line");
            final int questStepsCount = questStepsJson.length();
            final ArrayList<QuestStepInfo> questSteps = new ArrayList<>(questStepsCount);
            for(int j = 0; j < questStepsCount; j++) {
                questSteps.add(ObjectUtils.getModelFromJson(QuestStepInfo.class, questStepsJson.getJSONObject(j)));
            }
            quests.add(questSteps);
        }

        action = ObjectUtils.getModelFromJson(HeroActionInfo.class, json.getJSONObject("action"));

        position = ObjectUtils.getModelFromJson(PositionInfo.class, json.getJSONObject("position"));

        canPvp = json.getJSONObject("permissions").getBoolean("can_participate_in_pvp");
        canRepair = json.getJSONObject("permissions").getBoolean("can_repair_building");

        might = ObjectUtils.getModelFromJson(MightInfo.class, json.getJSONObject("might"));

        id = json.getInt("id");
        actualityTurnNumber = json.getInt("actual_on_turn");
        spriteId = json.getInt("sprite");

        companionInfo = ObjectUtils.getModelFromJson(CompanionInfo.class, json.optJSONObject("companion"));
    }

}
