package com.lonebytesoft.thetaleclient;

import android.text.TextUtils;

import com.lonebytesoft.thetaleclient.api.HttpMethod;
import com.lonebytesoft.thetaleclient.api.dictionary.Action;
import com.lonebytesoft.thetaleclient.api.dictionary.ArtifactEffect;
import com.lonebytesoft.thetaleclient.api.dictionary.ArtifactRarity;
import com.lonebytesoft.thetaleclient.api.dictionary.ArtifactType;
import com.lonebytesoft.thetaleclient.api.dictionary.CardRarity;
import com.lonebytesoft.thetaleclient.api.dictionary.CardType;
import com.lonebytesoft.thetaleclient.api.dictionary.CompanionFeature;
import com.lonebytesoft.thetaleclient.api.dictionary.CompanionFeatureType;
import com.lonebytesoft.thetaleclient.api.dictionary.CompanionSpecies;
import com.lonebytesoft.thetaleclient.api.dictionary.EquipmentType;
import com.lonebytesoft.thetaleclient.api.dictionary.GameState;
import com.lonebytesoft.thetaleclient.api.dictionary.Gender;
import com.lonebytesoft.thetaleclient.api.dictionary.Habit;
import com.lonebytesoft.thetaleclient.api.dictionary.HeroAction;
import com.lonebytesoft.thetaleclient.api.dictionary.Profession;
import com.lonebytesoft.thetaleclient.api.dictionary.QuestActorType;
import com.lonebytesoft.thetaleclient.api.dictionary.Race;
import com.lonebytesoft.thetaleclient.api.dictionary.Skill;
import com.lonebytesoft.thetaleclient.api.dictionary.ThirdPartyAuthState;
import com.lonebytesoft.thetaleclient.util.ObjectUtils;

import junit.framework.TestCase;

import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Hamster
 * @since 02.03.2015
 *
 * Tests for API dictionaries
 * Are not tested:
 * - Archetype {@see http://the-tale.org/guide/mobs/}
 * - ArtifactOrientation {@see http://the-tale.org/guide/artifacts/}
 * - ChatMilestone {@see chat scripts}
 * - CompanionDedication {@see http://the-tale.org/guide/companions/}
 * - CompanionFeatureType {@see http://the-tale.org/guide/companions/}
 * - CompanionRarity {@see http://the-tale.org/guide/companions/}
 * - CompanionType {@see http://the-tale.org/guide/companions/}
 * - HeroMode {@see http://the-tale.org/guide/api#game_info}
 * - MapCell dictionaries
 * - MapStyle {@see in-game map controls}
 * - MonsterType {@see http://the-tale.org/guide/mobs/}
 * - ProficiencyLevel {@see http://the-tale.org/guide/persons}
 * - PvpAbility {@see http://the-tale.org/guide/api#game_info}
 * - RatingItem {@see http://the-tale.org/guide/api#account_info}
 * - RoadDirection {@see map_info request}
 * - SkillAvailability {@see http://the-tale.org/guide/hero-abilities}
 * - SkillMethod {@see http://the-tale.org/guide/hero-abilities}
 * - SkillType {@see http://the-tale.org/guide/hero-abilities}
 */
public class TestApiDictionaries extends TestCase {

    private enum GuideApiTable {

        ACTION(9),
        ARTIFACT_EFFECT(3),
        ARTIFACT_RARITY(0),
        ARTIFACT_TYPE(1),
        CARD_RARITY(5),
        CARD_TYPE(4),
        EQUIPMENT_TYPE(2),
        GAME_STATE(16),
        GENDER(10),
        HABIT(12),
        HABIT_HONOR(13),
        HABIT_PEACEFULNESS(14),
        HERO_ACTION(6),
        PLACE_SPECIALIZATION(7),
        PROFESSION(17),
        QUEST_ACTOR_TYPE(8),
        RACE(11),
        SOCIAL_LINK(18),
        THIRD_PARTY_AUTH_STATE(15),
        ;

        public final int position;

        GuideApiTable(final int position) {
            this.position = position;
        }

    }

    public void testAction() {
        final Document document = getDocumentChecked("http://the-tale.org/guide/api");
        final Element elementTable = document.select("table").get(GuideApiTable.ACTION.position);
        final Elements elements = elementTable.select("td:first-child");
        checkSize(Action.class, elements.size());

        for(final Element elementCode : elements) {
            final String code = elementCode.text();
            assertNotNull(
                    String.format("Action not found: %s", code),
                    ObjectUtils.getEnumForCode(Action.class, code));
        }
    }

    public void testArtifactEffect() {
        Document document;
        Elements elements;

        document = getDocumentChecked("http://the-tale.org/guide/artifacts/");
        elements = document.select("div#pgf-effects tbody tr");
        checkSize(ArtifactEffect.class, elements.size() + 1, "name-description"); // "no effect" is not in list

        for(final Element element : elements) {
            final String name = element.child(0).text();
            final ArtifactEffect artifactEffect = ObjectUtils.getEnumForName(ArtifactEffect.class, name);
            assertNotNull(String.format("ArtifactEffect not found: name = %s", name), artifactEffect);

            final String description = element.child(1).text();
            assertEquals(String.format("ArtifactEffect incorrect description: %s", description),
                    description, artifactEffect.getDescription());
        }

        document = getDocumentChecked("http://the-tale.org/guide/api");
        final Element elementTable = document.select("table").get(GuideApiTable.ARTIFACT_EFFECT.position);
        elements = elementTable.select("tr");
        final int size = elements.size();
        checkSize(ArtifactEffect.class, size - 1, "code-name"); // exclude header

        for(int i = 1; i < size; i++) {
            final Element element = elements.get(i);

            final int code = Integer.parseInt(element.child(0).text());
            final ArtifactEffect artifactEffect = ObjectUtils.getEnumForCode(ArtifactEffect.class, code);
            assertNotNull(String.format("ArtifactEffect not found: code = %d", code), artifactEffect);

            final String name = element.child(1).text();
            assertEquals(String.format("ArtifactEffect incorrect name: %s", name),
                    name, artifactEffect.getName());
        }
    }

    public void testArtifactRarity() {
        final Document document = getDocumentChecked("http://the-tale.org/guide/api");
        final Element elementTable = document.select("table").get(GuideApiTable.ARTIFACT_RARITY.position);
        final Elements elements = elementTable.select("tr");
        final int size = elements.size();
        checkSize(ArtifactRarity.class, size - 1); // exclude header

        for(int i = 1; i < size; i++) {
            final Element element = elements.get(i);

            final int code = Integer.parseInt(element.child(0).text());
            final ArtifactRarity artifactRarity = ObjectUtils.getEnumForCode(ArtifactRarity.class, code);
            assertNotNull(String.format("ArtifactRarity not found: code = %d", code), artifactRarity);

            final String name = element.child(1).text();
            assertEquals(String.format("ArtifactRarity incorrect name: %s", name),
                    name, artifactRarity.getName());
        }
    }

    public void testArtifactType() {
        final Document document = getDocumentChecked("http://the-tale.org/guide/api");
        final Element elementTable = document.select("table").get(GuideApiTable.ARTIFACT_TYPE.position);
        final Elements elements = elementTable.select("tr");
        final int size = elements.size();
        checkSize(ArtifactType.class, size - 1); // exclude header

        for(int i = 1; i < size; i++) {
            final Element element = elements.get(i);

            final int code = Integer.parseInt(element.child(0).text());
            final ArtifactType artifactType = ObjectUtils.getEnumForCode(ArtifactType.class, code);
            assertNotNull(String.format("ArtifactType not found: code = %d", code), artifactType);

            final String name = element.child(1).text();
            assertEquals(String.format("ArtifactType incorrect name: %s", name),
                    name, artifactType.getName());
        }
    }

    public void testCardRarity() {
        final Document document = getDocumentChecked("http://the-tale.org/guide/api");
        final Element elementTable = document.select("table").get(GuideApiTable.CARD_RARITY.position);
        final Elements elements = elementTable.select("tr");
        final int size = elements.size();
        checkSize(CardRarity.class, size - 1); // exclude header

        for(int i = 1; i < size; i++) {
            final Element element = elements.get(i);

            final int code = Integer.parseInt(element.child(0).text());
            final CardRarity cardRarity = ObjectUtils.getEnumForCode(CardRarity.class, code);
            assertNotNull(String.format("CardRarity not found: code = %d", code), cardRarity);
        }
    }

    public void testCardType() {
        Document document;
        Elements elements;

        document = getDocumentChecked("http://the-tale.org/guide/cards/");
        elements = document.select("table tbody tr");
        checkSize(CardType.class, elements.size(), "name-description");

        for(final Element element : elements) {
            final String name = element.child(1).text();
            final CardType cardType = ObjectUtils.getEnumForName(CardType.class, name);
            assertNotNull(String.format("CardType not found: name = %s", name), cardType);

            final String description = element.child(2).text();
            assertEquals(String.format("CardType incorrect description: %s", description),
                    description, cardType.getDescription());
        }

        document = getDocumentChecked("http://the-tale.org/guide/api");
        final Element elementTable = document.select("table").get(GuideApiTable.CARD_TYPE.position);
        elements = elementTable.select("tr");
        final int size = elements.size();
        checkSize(CardType.class, size - 1, "code-name"); // exclude header

        for(int i = 1; i < size; i++) {
            final Element element = elements.get(i);

            final int code = Integer.parseInt(element.child(0).text());
            final CardType cardType = ObjectUtils.getEnumForCode(CardType.class, code);
            assertNotNull(String.format("CardType not found: code = %d", code), cardType);

            final String name = element.child(1).text();
            assertEquals(String.format("CardType incorrect name: %s", name),
                    name, cardType.getName());
        }
    }

    public void testCompanionFeature() {
        final Document document = getDocumentChecked("http://the-tale.org/guide/companions/");
        final Elements elements = document.select("div#pgf-companion-effects-accordion table tbody tr");
        checkSize(CompanionFeature.class, elements.size(), "name-description");

        for(final Element element : elements) {
            final String name = element.child(0).text();
            final CompanionFeature companionFeature = ObjectUtils.getEnumForName(CompanionFeature.class, name);
            assertNotNull(String.format("CompanionFeature not found: name = %s", name), companionFeature);

            final String type = element.child(1).text();
            assertNotNull(String.format("CompanionFeatureType not found: name = %s", type),
                    ObjectUtils.getEnumForName(CompanionFeatureType.class, type));

            final String description = element.child(2).text();
            assertEquals(String.format("CompanionFeature incorrect description: %s", description),
                    description, companionFeature.getDescription());
        }
    }

    public void testCompanionSpecies() {
        final Document documentList = getDocumentChecked("http://the-tale.org/guide/companions/");
        final Elements elementsList = documentList.select("table.table td a");
        checkSize(CompanionSpecies.class, elementsList.size() / 2); // each companion row has 2 links

        for(final Element elementList : elementsList) {
            final String url = elementList.attr("href");
            final int id;
            try {
                id = Integer.parseInt(url.substring(url.lastIndexOf('/') + 1));
            } catch (NumberFormatException ignored) {
                continue;
            }

            final CompanionSpecies companionSpecies = ObjectUtils.getEnumForCode(CompanionSpecies.class, id);
            assertNotNull(String.format("CompanionSpecies not found: id = %d", id), companionSpecies);

            final Document document = getDocumentChecked(String.format("http://the-tale.org/guide/companions/%d/info", id));
            final Elements elements = document.select("table td");

            final String name = document.select(".dialog-title").first().text();
            assertEquals(String.format("CompanionSpecies id = %d incorrect name: %s", id, name),
                    name, companionSpecies.name);

            final String rarity = elements.get(0).text();
            assertEquals(String.format("CompanionSpecies %s incorrect rarity: %s", name, rarity),
                    rarity, companionSpecies.rarity.getName());

            final String type = elements.get(1).text();
            assertEquals(String.format("CompanionSpecies %s incorrect type: %s", name, type),
                    type, companionSpecies.type.getName());

            final String archetype = elements.get(2).text();
            assertEquals(String.format("CompanionSpecies %s incorrect archetype: %s", name, archetype),
                    archetype, companionSpecies.archetype.getCode());

            final String dedication = elements.get(3).text();
            assertEquals(String.format("CompanionSpecies %s incorrect dedication: %s", name, dedication),
                    dedication, companionSpecies.dedication.getName());

            final int health = Integer.parseInt(elements.get(4).text());
            assertEquals(String.format("CompanionSpecies %s incorrect health: %d", name, health),
                    health, companionSpecies.health);

            final Map<CompanionFeature, Integer> features = new HashMap<>();

            final String[] featuresInitial = elements.get(5).text().split(", ");
            for(final String feature : featuresInitial) {
                features.put(ObjectUtils.getEnumForName(CompanionFeature.class, feature), 0);
            }

            final Elements featuresLate = elements.get(6).select("li");
            final Pattern pattern = Pattern.compile("[^\\d]*(\\d+):\\s*(.+)");
            for(final Element feature : featuresLate) {
                final Matcher matcher = pattern.matcher(feature.text());
                if(matcher.find()) {
                    features.put(
                            ObjectUtils.getEnumForName(CompanionFeature.class, matcher.group(2)),
                            Integer.parseInt(matcher.group(1)));
                } else {
                    fail(String.format("Could not parse companion id = %d features: %s", id, feature.text()));
                }
            }

            assertEquals(String.format("CompanionSpecies %s incorrect features", name),
                    features, companionSpecies.features);

            assertEquals(String.format("CompanionSpecies %s incorrect description", name),
                    document.select("blockquote").first().html(), companionSpecies.description);
        }
    }

    public void testEquipmentType() {
        final Document document = getDocumentChecked("http://the-tale.org/guide/api");
        final Element elementTable = document.select("table").get(GuideApiTable.EQUIPMENT_TYPE.position);
        final Elements elements = elementTable.select("tr");
        final int size = elements.size();
        checkSize(EquipmentType.class, size - 1); // exclude header

        for(int i = 1; i < size; i++) {
            final Element element = elements.get(i);

            final int code = Integer.parseInt(element.child(0).text());
            final EquipmentType equipmentType = ObjectUtils.getEnumForCode(EquipmentType.class, code);
            assertNotNull(String.format("EquipmentType not found: code = %d", code), equipmentType);
        }
    }

    public void testGameState() {
        final Document document = getDocumentChecked("http://the-tale.org/guide/api");
        final Element elementTable = document.select("table").get(GuideApiTable.GAME_STATE.position);
        final Elements elements = elementTable.select("tr");
        final int size = elements.size();
        checkSize(GameState.class, size - 1); // exclude header

        for(int i = 1; i < size; i++) {
            final Element element = elements.get(i);

            final int code = Integer.parseInt(element.child(0).text());
            final GameState gameState = ObjectUtils.getEnumForCode(GameState.class, code);
            assertNotNull(String.format("GameState not found: code = %d", code), gameState);
        }
    }

    public void testGender() {
        final Document document = getDocumentChecked("http://the-tale.org/guide/api");
        final Element elementTable = document.select("table").get(GuideApiTable.GENDER.position);
        final Elements elements = elementTable.select("tr");
        final int size = elements.size();
        checkSize(Gender.class, size - 1); // exclude header

        for(int i = 1; i < size; i++) {
            final Element element = elements.get(i);

            final int code = Integer.parseInt(element.child(0).text());
            final Gender gender = ObjectUtils.getEnumForCode(Gender.class, code);
            assertNotNull(String.format("Gender not found: code = %d", code), gender);

            final String name = element.child(1).text();
            assertEquals(String.format("Gender incorrect name: %s", name),
                    name, gender.getName());
        }
    }

    public void testHabit() {
        final Document document = getDocumentChecked("http://the-tale.org/guide/api");
        final Element elementTable = document.select("table").get(GuideApiTable.HABIT.position);
        final Elements elements = elementTable.select("tr");
        final int size = elements.size();
        checkSize(Habit.class, size - 1); // exclude header

        for(int i = 1; i < size; i++) {
            final Element element = elements.get(i);

            final int code = Integer.parseInt(element.child(0).text());
            final Habit habit = ObjectUtils.getEnumForCode(Habit.class, code);
            assertNotNull(String.format("Habit not found: code = %s", code), habit);

            final String name = element.child(1).text();
            assertEquals(String.format("Habit incorrect name: %s", name),
                    name, habit.name);

            final String description = element.child(2).text();
            assertEquals(String.format("Habit incorrect description: %s", description),
                    description, habit.description);
        }
    }

    public void testHeroAction() {
        final Document document = getDocumentChecked("http://the-tale.org/guide/api");
        final Element elementTable = document.select("table").get(GuideApiTable.HERO_ACTION.position);
        final Elements elements = elementTable.select("tr");
        final int size = elements.size();
        checkSize(HeroAction.class, size - 1); // exclude header

        for(int i = 1; i < size; i++) {
            final Element element = elements.get(i);

            final int code = Integer.parseInt(element.child(0).text());
            final HeroAction heroAction = ObjectUtils.getEnumForCode(HeroAction.class, code);
            assertNotNull(String.format("HeroAction not found: code = %d", code), heroAction);
        }
    }

    public void testProfession() {
        final Document document = getDocumentChecked("http://the-tale.org/guide/api");
        final Element elementTable = document.select("table").get(GuideApiTable.PROFESSION.position);
        final Elements elements = elementTable.select("tr");
        final int size = elements.size();
        checkSize(Profession.class, size - 1); // exclude header

        for(int i = 1; i < size; i++) {
            final Element element = elements.get(i);

            final int code = Integer.parseInt(element.child(0).text());
            final Profession profession = ObjectUtils.getEnumForCode(Profession.class, code);
            assertNotNull(String.format("Profession not found: code = %d", code), profession);

            final String name = element.child(1).text();
            assertEquals(String.format("Profession incorrect name: %s", name),
                    name, profession.getName());
        }
    }

    public void testQuestActorType() {
        final Document document = getDocumentChecked("http://the-tale.org/guide/api");
        final Element elementTable = document.select("table").get(GuideApiTable.QUEST_ACTOR_TYPE.position);
        final Elements elements = elementTable.select("tr");
        final int size = elements.size();
        checkSize(QuestActorType.class, size - 1); // exclude header

        for(int i = 1; i < size; i++) {
            final Element element = elements.get(i);

            final int code = Integer.parseInt(element.child(0).text());
            final QuestActorType questActorType = ObjectUtils.getEnumForCode(QuestActorType.class, code);
            assertNotNull(String.format("QuestActorType not found: code = %d", code), questActorType);
        }
    }

    public void testRace() {
        final Document document = getDocumentChecked("http://the-tale.org/guide/api");
        final Element elementTable = document.select("table").get(GuideApiTable.RACE.position);
        final Elements elements = elementTable.select("tr");
        final int size = elements.size();
        checkSize(Race.class, size - 1); // exclude header

        for(int i = 1; i < size; i++) {
            final Element element = elements.get(i);

            final int code = Integer.parseInt(element.child(0).text());
            final Race race = ObjectUtils.getEnumForCode(Race.class, code);
            assertNotNull(String.format("Race not found: code = %d", code), race);

            final String name = element.child(1).text();
            assertEquals(String.format("Race incorrect name: %s", name),
                    name, race.getName());
        }
    }

    public void testSkill() {
        final Document document = getDocumentChecked("http://the-tale.org/guide/hero-abilities");
        final Elements elements = document.select(".hero-abilities-list .hero-ability-record");
        checkSize(Skill.class, elements.size());

        for(final Element element : elements) {
            final String name = element.child(0).ownText();
            final Skill skill = ObjectUtils.getEnumForName(Skill.class, name);
            assertNotNull(String.format("Skill not found: name = %s", name), skill);

            final String description = element.child(1).ownText();
            assertEquals(String.format("Skill %s incorrect description: %s", name, description),
                    description, skill.description);

            final String[] titleItems = element.attr("title").split(",");

            final String levelString = titleItems[0].trim();
            final int level = Integer.parseInt(levelString.substring(levelString.lastIndexOf(' ') + 1));
            assertEquals(String.format("Skill %s incorrect max level: %d", name, level),
                    level, skill.maxLevel);

            final String type = titleItems[1].trim();
            assertEquals(String.format("Skill %s incorrect type: %s", name, type),
                    type, skill.type.getCode());

            final String availability = titleItems[2].trim();
            assertEquals(String.format("Skill %s incorrect availability: %s", name, availability),
                    availability, skill.availability.getCode());

            final String method = titleItems[3].trim();
            assertEquals(String.format("Skill %s incorrect method: %s", name, method),
                    method, skill.method.getCode());

            final String direct = titleItems.length > 4 ? titleItems[4].trim() : null;
            final boolean isDirect = (direct != null) && direct.equals("прямой урон");
            assertEquals(String.format("Skill %s incorrect is direct damage: %s", name, isDirect),
                    isDirect, skill.isDirectDamage);
        }
    }

    public void testThirdPartyAuthState() {
        final Document document = getDocumentChecked("http://the-tale.org/guide/api");
        final Element elementTable = document.select("table").get(GuideApiTable.THIRD_PARTY_AUTH_STATE.position);
        final Elements elements = elementTable.select("tr");
        final int size = elements.size();
        checkSize(ThirdPartyAuthState.class, size - 1); // exclude header

        for(int i = 1; i < size; i++) {
            final Element element = elements.get(i);

            final int code = Integer.parseInt(element.child(0).text());
            final ThirdPartyAuthState thirdPartyAuthState = ObjectUtils.getEnumForCode(ThirdPartyAuthState.class, code);
            assertNotNull(String.format("ThirdPartyAuthState not found: code = %d", code), thirdPartyAuthState);

            final String name = element.child(1).text().toLowerCase();
            assertEquals(String.format("ThirdPartyAuthState incorrect name: %s", name),
                    name, thirdPartyAuthState.getDescription().toLowerCase());
        }
    }

    private Document getDocument(final String url) {
        final HttpMethod httpMethod = HttpMethod.GET;
        final HttpClient httpClient = new DefaultHttpClient();
        final HttpRequest httpRequest = httpMethod.getHttpRequest(url, null, null);

        try {
            final OutputStream outputStream = new ByteArrayOutputStream();
            httpClient.execute((HttpUriRequest) httpRequest).getEntity().writeTo(outputStream);
            outputStream.close();

            final String html = outputStream.toString();
            if(TextUtils.isEmpty(html)) {
                return null;
            }

            return Jsoup.parse(outputStream.toString());
        } catch(IOException e) {
            return null;
        }
    }

    private Document getDocumentChecked(final String url) {
        final Document document = getDocument(url);
        assertNotNull(String.format("Document getting failed: %s", url), document);
        return document;
    }

    private <E extends Enum<E>> void checkSize(final Class<E> clazz, final int expected) {
        final int actual = clazz.getEnumConstants().length;
        assertEquals(
                String.format("%s: incorrect values count; expected %d, actual %d",
                        clazz.getSimpleName(), expected, actual),
                expected, actual);
    }

    private <E extends Enum<E>> void checkSize(final Class<E> clazz, final int expected, final String comment) {
        final int actual = clazz.getEnumConstants().length;
        assertEquals(
                String.format("%s: incorrect values count; expected %d, actual %d - %s",
                        clazz.getSimpleName(), expected, actual, comment),
                expected, actual);
    }

}
